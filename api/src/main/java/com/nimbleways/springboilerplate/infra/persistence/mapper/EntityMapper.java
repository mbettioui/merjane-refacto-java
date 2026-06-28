package com.nimbleways.springboilerplate.infra.persistence.mapper;

import com.nimbleways.springboilerplate.domain.model.Order;
import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.infra.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infra.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public Product toDomain(ProductEntity e) {
        Product p = new Product();
        p.setId(e.getId());
        p.setLeadTime(e.getLeadTime());
        p.setAvailable(e.getAvailable());
        p.setType(ProductType.from(e.getType()));
        p.setName(e.getName());
        p.setExpiryDate(e.getExpiryDate());
        p.setSeasonStartDate(e.getSeasonStartDate());
        p.setSeasonEndDate(e.getSeasonEndDate());
        return p;
    }

    public void applyToEntity(Product p, ProductEntity e) {
        e.setLeadTime(p.getLeadTime());
        e.setAvailable(p.getAvailable());
    }

    public Order toDomain(OrderEntity e) {
        Order o = new Order();
        o.setId(e.getId());
        o.setItems(e.getItems().stream().map(this::toDomain).collect(Collectors.toSet()));
        return o;
    }
}
