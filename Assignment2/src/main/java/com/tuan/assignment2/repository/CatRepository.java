package com.tuan.assignment2.repository;

import com.tuan.assignment2.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatRepository extends JpaRepository<Cat,Integer> {
}
