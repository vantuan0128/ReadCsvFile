package com.tuan.assignment2.mapper;

import com.tuan.assignment2.entity.Cat;
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
public class CatCsvMapper implements CsvMapper<Cat>{

    CsvService csvService;

    @Override
    public List<Cat> mapCsv(InputStream inputStream) throws IOException {
        return csvService.readCsvFile(inputStream, Cat.class);
    }
}
