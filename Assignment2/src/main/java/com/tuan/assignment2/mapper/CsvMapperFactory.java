package com.tuan.assignment2.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvMapperFactory {

    private final Map<String, CsvMapper<?>> mapperRegistry;

    @Autowired
    public CsvMapperFactory(List<CsvMapper<?>> mappers) {
        this.mapperRegistry = new HashMap<>();
        mappers.forEach(mapper -> {
            if (mapper instanceof UserCsvMapper) {
                mapperRegistry.put("User", mapper);
            } else if (mapper instanceof CatCsvMapper) {
                mapperRegistry.put("Cat", mapper);
            }
        });
    }

    public CsvMapper<?> getMapper(String entityType) {
        return mapperRegistry.get(entityType);
    }
}

