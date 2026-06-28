package com.nimbleways.springboilerplate.infra.persistence.adapter;

import com.nimbleways.springboilerplate.domain.model.Order;
import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.infra.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infra.persistence.entity.ProductEntity;
import com.nimbleways.springboilerplate.infra.persistence.mapper.EntityMapper;
import com.nimbleways.springboilerplate.infra.persistence.repository.OrderRepository;
import com.nimbleways.springboilerplate.infra.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdapterTest {

    @Mock ProductRepository productRepository;
    @Mock OrderRepository orderRepository;
    @Mock EntityMapper mapper;

    @Test
    void productAdapter_save_appliesAndPersists() {
        ProductEntity entity = new ProductEntity(1L, 5, 10, "NORMAL", "Cable", null, null, null);
        Product domain = new Product();
        domain.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productRepository.save(entity)).thenReturn(entity);

        ProductAdapter adapter = new ProductAdapter(productRepository, mapper);
        Product result = adapter.save(domain);

        verify(mapper).applyToEntity(domain, entity);
        verify(productRepository).save(entity);
        assertSame(domain, result);
    }

    @Test
    void orderAdapter_findById_mapsEntity() {
        ProductEntity pe = new ProductEntity(1L, 5, 10, "NORMAL", "Cable", null, null, null);
        OrderEntity oe = new OrderEntity(42L, Set.of(pe));
        Order expected = new Order();
        expected.setId(42L);
        when(orderRepository.findById(42L)).thenReturn(Optional.of(oe));
        when(mapper.toDomain(oe)).thenReturn(expected);

        OrderAdapter adapter = new OrderAdapter(orderRepository, mapper);
        Optional<Order> result = adapter.findById(42L);

        assertTrue(result.isPresent());
        assertEquals(42L, result.get().getId());
    }

    @Test
    void orderAdapter_findById_emptyWhenNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        OrderAdapter adapter = new OrderAdapter(orderRepository, mapper);
        assertTrue(adapter.findById(99L).isEmpty());
    }
}
