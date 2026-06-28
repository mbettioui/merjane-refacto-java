package com.nimbleways.springboilerplate.infra.persistence.repository;

import com.nimbleways.springboilerplate.infra.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
