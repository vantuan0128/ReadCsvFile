package com.tuan.assignment2.entity;

import com.tuan.assignment2.validator.CsvColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cat {
    @Id
    @CsvColumn(name = "ID")
    int id;

    @CsvColumn(name = "Number of legs")
    int numberOfLegs;

    @CsvColumn(name = "Blood group")
    char bloodGroup;

    @CsvColumn(name = "Name of owner")
    String nameOfOwner;

    @CsvColumn(name = "Date of birth")
    LocalDateTime dateOfBirth;
}
