package com.tun.assignment2.service;

import com.tun.assignment2.util.ReflectionUtils;
import com.tun.assignment2.validator.CsvColumn;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CsvService {

    public static <T> List<T> readCsvFile(InputStream inputStream, Class<T> clazz) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        List<T> objects = new ArrayList<>();

        String headerLine = reader.readLine();
        String[] headers = headerLine.split(",");

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            try {
                T object = clazz.getDeclaredConstructor().newInstance();
                Field[] fields = ReflectionUtils.getAnnotatedFields(clazz);

                for (int i = 0; i < headers.length; i++) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(CsvColumn.class)) {
                            CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                            if (annotation.name().equals(headers[i])) {
                                Object value = parseValue(values[i], field.getType());
                                ReflectionUtils.setFieldValue(object, field, value);
                            }
                        }
                    }
                }
                objects.add(object);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        reader.close();
        return objects;
    }

    private static Object parseValue(String value, Class<?> type) {
        if (type == int.class) {
            return Integer.parseInt(value);
        } else if (type == String.class) {
            return value;
        }
        throw new IllegalArgumentException("Kiểu dữ liệu không hỗ trợ: " + type);
    }

    public static void writeCsvFile(List<?> objects, OutputStream outputStream) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));

        if (objects.isEmpty()) {
            throw new IllegalArgumentException("Danh sách trống!");
        }

        Class<?> clazz = objects.get(0).getClass();
        Field[] fields = ReflectionUtils.getAnnotatedFields(clazz);

        // Ghi header CSV
        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                writer.print(annotation.name() + ",");
            }
        }
        writer.println();

        // Ghi dữ liệu
        for (Object object : objects) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(CsvColumn.class)) {
                    Object value = ReflectionUtils.getFieldValue(object, field);
                    writer.print(value + ",");
                }
            }
            writer.println();
        }

        writer.flush();
        writer.close();
    }
}
