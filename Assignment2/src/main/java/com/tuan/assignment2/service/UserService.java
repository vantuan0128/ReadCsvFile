package com.tuan.assignment2.service;

import com.tuan.assignment2.entity.User;
import com.tuan.assignment2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IService<User> {

    UserRepository userRepository;

    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
