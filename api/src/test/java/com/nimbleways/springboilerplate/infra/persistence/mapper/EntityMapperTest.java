package com.nimbleways.springboilerplate.infra.persistence.mapper;

import com.nimbleways.springboilerplate.domain.model.Order;
import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.infra.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infra.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    private final EntityMapper mapper = new EntityMapper();

    @Test
    void toDomain_product_mapsAllFields() {
        LocalDate expiry = LocalDate.now().plusDays(5);
        LocalDate start = LocalDate.now().minusDays(2);
        LocalDate end = LocalDate.now().plusDays(30);
        ProductEntity e = new ProductEntity(1L, 5, 10, "SEASONAL", "Watermelon", expiry, start, end);

        Product p = mapper.toDomain(e);

        assertEquals(1L, p.getId());
        assertEquals(5, p.getLeadTime());
        assertEquals(10, p.getAvailable());
        assertEquals(ProductType.SEASONAL, p.getType());
        assertEquals("Watermelon", p.getName());
        assertEquals(expiry, p.getExpiryDate());
        assertEquals(start, p.getSeasonStartDate());
        assertEquals(end, p.getSeasonEndDate());
    }

    @Test
    void applyToEntity_updatesAvailableAndLeadTime() {
        ProductEntity e = new ProductEntity(1L, 10, 5, "NORMAL", "Cable", null, null, null);
        Product p = new Product();
        p.setAvailable(4);
        p.setLeadTime(7);

        mapper.applyToEntity(p, e);

        assertEquals(4, e.getAvailable());
        assertEquals(7, e.getLeadTime());
    }

    @Test
    void toDomain_order_mapsIdAndItems() {
        ProductEntity pe = new ProductEntity(1L, 5, 10, "NORMAL", "Item", null, null, null);
        OrderEntity oe = new OrderEntity(42L, Set.of(pe));

        Order o = mapper.toDomain(oe);

        assertEquals(42L, o.getId());
        assertEquals(1, o.getItems().size());
        assertEquals(ProductType.NORMAL, o.getItems().iterator().next().getType());
    }
}
