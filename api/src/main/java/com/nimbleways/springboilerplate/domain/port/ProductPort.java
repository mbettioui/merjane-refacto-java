package com.nimbleways.springboilerplate.domain.port;

import com.nimbleways.springboilerplate.domain.model.Product;

public interface ProductPort {
    Product save(Product product);
}
