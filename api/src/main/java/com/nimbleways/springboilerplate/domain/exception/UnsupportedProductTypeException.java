package com.nimbleways.springboilerplate.domain.exception;

import com.nimbleways.springboilerplate.domain.model.ProductType;

public class UnsupportedProductTypeException extends RuntimeException {
    public UnsupportedProductTypeException(ProductType type) {
        super("No handler for product type: " + type);
    }
}
