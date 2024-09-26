package com.tuan.assignment2.service;

import com.tuan.assignment2.entity.Cat;
import com.tuan.assignment2.repository.CatRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CatService implements IService<Cat>{

    CatRepository catRepository;

    @Override
    public void saveAll(List<Cat> cats) {
        catRepository.saveAll(cats);
    }

    @Override
    public List<Cat> findAll() {
        return catRepository.findAll();
    }
}
