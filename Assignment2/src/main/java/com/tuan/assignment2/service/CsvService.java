package com.tuan.assignment2.service;

import com.tuan.assignment2.logger.CsvLogger;
import com.tuan.assignment2.logger.Logger;
import com.tuan.assignment2.util.EmailUtils;
import com.tuan.assignment2.util.ReflectionUtils;
import com.tuan.assignment2.validator.CsvColumn;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service

public class CsvService {

    @Autowired
    private ReflectionUtils reflectionUtils;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private Logger logger;

    @Autowired
    private ServiceFactory serviceFactory;

    @PostConstruct
    public void init() {
        logger = new CsvLogger();
        try {
            logger.initializerLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> readCsvFile(InputStream inputStream, Class<T> clazz) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<T> objects = new ArrayList<>();

        String headerLine = reader.readLine();
        String[] headers = headerLine.split(",");

        String line;
        int lineNumber = 1;
        while((line = reader.readLine()) != null) {
            lineNumber++;
            String[] values = line.split(",");
            T object = processLine(values, headers, clazz, lineNumber);
            if (object != null) {
                objects.add(object);
            }
        }

        reader.close();
        return objects;
    }

    private <T> T processLine(String[] values, String[] headers, Class<T> clazz, int lineNumber) {
        try {
            T object = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            boolean isValid = true;

            for (int i = 0; i < headers.length; i++) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(CsvColumn.class)) {
                        CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                        if (annotation.name().equals(headers[i])) {
                            Object value = parseValue(values[i], field.getType());

                            if (annotation.name().equals("Email") && !emailUtils.isValidEmail(value.toString())) {
                                isValid = false;
                                logger.logWarning(lineNumber, "Email is invalid - " + values[i]);
                                break;
                            }

                            reflectionUtils.setFieldValue(object, field, value);
                        }
                    }
                }

                if (!isValid) break;
            }

            return isValid ? object : null;
        } catch (Exception e) {
            logger.logError(lineNumber, e);
            return null;
        }
    }

    private Object parseValue(String value, Class<?> type) {
        if (type == int.class) {
            return Integer.parseInt(value);
        } else if (type == String.class) {
            return value;
        } else if (type == char.class) {
            return value.charAt(0);
        } else if (type == LocalDateTime.class) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return LocalDateTime.parse(value, formatter);
        }
        throw new IllegalArgumentException("Unsupported data types: " + type);
    }

    public void writeCsvFile(List<?> objects, OutputStream outputStream) throws UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        if (objects.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }

        Class<?> clazz = objects.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                writer.print(annotation.name() + ",");
            }
        }
        writer.println();

        for (Object object : objects) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(CsvColumn.class)) {
                    Object value = reflectionUtils.getFieldValue(object, field);
                    writer.print(value + ",");
                }
            }
            writer.println();
        }

        writer.flush();
        writer.close();
    }

    public void exportAllToCsv(OutputStream outputStream) throws UnsupportedEncodingException {
        Map<String, List<?>> dataMap = new HashMap<>();

        List<String> entityTypes = Arrays.asList("User", "Cat");

        for (String entityType : entityTypes) {
            IService<?> service = serviceFactory.getService(entityType);
            if (service != null) {
                List<?> entities = service.findAll();
                if (entities != null) {
                    dataMap.put(entityType, entities);
                } else {
                    logger.logWarning(1,"Service for entity type " + entityType + " returned null.");
                }
            }
        }
        System.out.println("Data Map: " + dataMap);
        writeMultipleCsvFiles(dataMap, outputStream);
    }

    public void writeMultipleCsvFiles(Map<String, List<?>> dataMap, OutputStream outputStream) throws UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        for (Map.Entry<String, List<?>> entry : dataMap.entrySet()) {
            String tableName = entry.getKey();
            List<?> objects = entry.getValue();

            if (objects.isEmpty()) {
                logger.logWarning(2,"No data to write for entity type: " + tableName);
                continue;
            }

            Class<?> clazz = objects.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();

            writer.println(tableName);
            for (Field field : fields) {
                if (field.isAnnotationPresent(CsvColumn.class)) {
                    CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                    writer.print(annotation.name() + ",");
                }
            }
            writer.println();

            for (Object object : objects) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(CsvColumn.class)) {
                        Object value = reflectionUtils.getFieldValue(object, field);
                        writer.print(value + ",");
                    }
                }
                writer.println();
            }

            writer.println();
        }

        writer.flush();
        writer.close();
    }


}
