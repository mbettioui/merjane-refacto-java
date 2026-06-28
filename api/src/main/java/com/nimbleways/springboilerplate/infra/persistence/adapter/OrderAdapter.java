package com.nimbleways.springboilerplate.infra.persistence.adapter;

import com.nimbleways.springboilerplate.domain.model.Order;
import com.nimbleways.springboilerplate.domain.port.OrderPort;
import com.nimbleways.springboilerplate.infra.persistence.mapper.EntityMapper;
import com.nimbleways.springboilerplate.infra.persistence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderAdapter implements OrderPort {

    private final OrderRepository orderRepository;
    private final EntityMapper mapper;

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId).map(mapper::toDomain);
    }
}
