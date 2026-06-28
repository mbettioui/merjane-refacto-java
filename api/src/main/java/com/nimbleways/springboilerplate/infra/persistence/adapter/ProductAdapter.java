package com.nimbleways.springboilerplate.infra.persistence.adapter;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.port.ProductPort;
import com.nimbleways.springboilerplate.infra.persistence.entity.ProductEntity;
import com.nimbleways.springboilerplate.infra.persistence.mapper.EntityMapper;
import com.nimbleways.springboilerplate.infra.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;
    private final EntityMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productRepository.findById(product.getId()).orElseThrow();
        mapper.applyToEntity(product, entity);
        productRepository.save(entity);
        return product;
    }
}
