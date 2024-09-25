package com.tun.assignment2.controller;

import com.tun.assignment2.entity.Customer;
import com.tun.assignment2.service.CsvService;
import com.tun.assignment2.service.CustomerService;
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

import java.io.IOException;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerController {

    CustomerService customerService;

    @PostMapping("/upload-csv")
    public String uploadCsvFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "File không được để trống!");
            return "uploadCsv";
        }

        try {
            List<Customer> customers = CsvService.readCsvFile(file.getInputStream(), Customer.class);
            customerService.saveAll(customers); // Lưu dữ liệu vào database
            model.addAttribute("message", "Upload thành công!");
        } catch (IOException e) {
            model.addAttribute("message", "Có lỗi khi đọc file CSV: " + e.getMessage());
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
        response.setHeader("Content-Disposition", "attachment; filename=customers.csv");

        List<Customer> customers = customerService.findAll(); // Lấy tất cả khách hàng từ database

        try {
            CsvService.writeCsvFile(customers, response.getOutputStream());
        } catch (IOException e) {
            // Xử lý lỗi nếu cần
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
