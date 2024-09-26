package com.tuan.assignment2.mapper;

import com.tuan.assignment2.entity.User;
import com.tuan.assignment2.service.CsvService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class  UserCsvMapper implements CsvMapper<User>{

    CsvService csvService;

    @Override
    public List<User> mapCsv(InputStream inputStream) throws IOException {
        return csvService.readCsvFile(inputStream, User.class);
    }
}
