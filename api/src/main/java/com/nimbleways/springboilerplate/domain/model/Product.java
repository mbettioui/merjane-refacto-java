package com.nimbleways.springboilerplate.domain.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    private Long id;
    private Integer leadTime;
    private Integer available;
    private ProductType type;
    private String name;
    private LocalDate expiryDate;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
}
