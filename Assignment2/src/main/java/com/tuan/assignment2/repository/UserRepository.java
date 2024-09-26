package com.tuan.assignment2.repository;

import com.tuan.assignment2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
