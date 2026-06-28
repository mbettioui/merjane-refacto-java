package com.nimbleways.springboilerplate.domain.model;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private Long id;
    private Set<Product> items;
}
