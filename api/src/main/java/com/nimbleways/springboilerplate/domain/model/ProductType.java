package com.nimbleways.springboilerplate.domain.model;

import java.util.Arrays;

public enum ProductType {
    NORMAL,
    SEASONAL,
    EXPIRABLE;

    public static ProductType from(String raw) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(raw))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown product type: " + raw));
    }
}
