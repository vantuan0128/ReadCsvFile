package com.tuan.assignment2.controller;

import com.tuan.assignment2.mapper.CsvMapper;
import com.tuan.assignment2.mapper.CsvMapperFactory;
import com.tuan.assignment2.service.CsvService;
import com.tuan.assignment2.service.IService;
import com.tuan.assignment2.service.ServiceFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MainController {

    CsvService csvService;

    CsvMapperFactory csvMapperFactory;
    ServiceFactory serviceFactory;

    @PostMapping("/upload-csv")
    public String uploadCsvFile(@RequestParam("file") MultipartFile file,
                                @RequestParam("entityType") String entityType,
                                Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "File must not be left blank");
            return "uploadCsv";
        }

        try {
            CsvMapper<?> mapper = csvMapperFactory.getMapper(entityType);
            if (mapper == null) {
                model.addAttribute("message", "Unsupported entity type");
                return "uploadCsv";
            }

            IService<?> service = serviceFactory.getService(entityType);
            if (service == null) {
                model.addAttribute("message", "No service available for entity type: " + entityType);
                return "uploadCsv";
            }

            List<?> entities = mapper.mapCsv(file.getInputStream());
            List<Object> entityList = (List<Object>) entities;

            ((IService<Object>) service).saveAll(entityList);

            model.addAttribute("message", "Upload success!");
        } catch (IOException e) {
            model.addAttribute("message", "There was an error when reading the CSV file: " + e.getMessage());
        }

        return "uploadCsv";
    }

    @GetMapping("/upload-csv")
    public String showUploadCsvPage() {
        return "uploadCsv";
    }

    @GetMapping("/export-csv")
    @ResponseBody
    public void exportCsvFile(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=output.csv");

        // Write local
//        try (OutputStream outputStream = new FileOutputStream("output.csv")) {
//            csvService.exportAllToCsv(outputStream);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try (OutputStream outputStream = response.getOutputStream()) {
            csvService.exportAllToCsv(outputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
