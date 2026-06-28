package com.nimbleways.springboilerplate.domain.handler;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;

public interface ProductHandler {
    ProductType supportedType();
    void process(Product product);
}
