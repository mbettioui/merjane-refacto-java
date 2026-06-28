package com.nimbleways.springboilerplate.infra.persistence.repository;

import com.nimbleways.springboilerplate.infra.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
