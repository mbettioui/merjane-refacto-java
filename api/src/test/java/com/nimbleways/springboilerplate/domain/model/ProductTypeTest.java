package com.nimbleways.springboilerplate.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @Test
    void from_validValues_casInsensitive() {
        assertEquals(ProductType.NORMAL, ProductType.from("NORMAL"));
        assertEquals(ProductType.NORMAL, ProductType.from("normal"));
        assertEquals(ProductType.SEASONAL, ProductType.from("Seasonal"));
        assertEquals(ProductType.EXPIRABLE, ProductType.from("EXPIRABLE"));
    }

    @Test
    void from_unknownValue_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ProductType.from("UNKNOWN"));
    }
}
