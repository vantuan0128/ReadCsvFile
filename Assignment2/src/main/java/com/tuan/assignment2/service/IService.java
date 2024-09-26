package com.tuan.assignment2.service;

import java.util.List;

public interface IService<T> {
    void saveAll(List<T> entities);

    List<T> findAll();
}
