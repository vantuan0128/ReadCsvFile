package com.tuan.assignment2.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CsvMapper<T> {
    List<T> mapCsv(InputStream inputStream) throws IOException;
}
