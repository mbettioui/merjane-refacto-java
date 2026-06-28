package com.nimbleways.springboilerplate.domain.port;

import com.nimbleways.springboilerplate.domain.model.Order;

import java.util.Optional;

public interface OrderPort {
    Optional<Order> findById(Long orderId);
}
