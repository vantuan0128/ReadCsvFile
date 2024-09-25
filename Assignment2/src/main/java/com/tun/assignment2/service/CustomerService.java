package com.tun.assignment2.service;

import com.tun.assignment2.entity.Customer;
import com.tun.assignment2.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerService {
    CustomerRepository customerRepository;

    public void saveAll(List<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
