package com.tun.assignment2.entity;

import com.tun.assignment2.validator.CsvColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Customer {
    @Id
    @CsvColumn(name = "ID")
    int id;

    @CsvColumn(name = "Full Name")
    String fullName;

    @CsvColumn(name = "Age")
    int age;
}
