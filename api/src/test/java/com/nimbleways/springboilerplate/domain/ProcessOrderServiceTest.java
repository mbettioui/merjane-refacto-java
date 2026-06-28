package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductTypeException;
import com.nimbleways.springboilerplate.domain.handler.ProductHandler;
import com.nimbleways.springboilerplate.domain.model.Order;
import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.port.OrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessOrderServiceTest {

    @Mock OrderPort orderPort;
    @Mock ProductHandler handler;
    ProcessOrderService service;

    @BeforeEach
    void setUp() {
        when(handler.supportedType()).thenReturn(ProductType.NORMAL);
        service = new ProcessOrderService(orderPort, List.of(handler));
    }

    @Test
    void processOrder_validOrder_dispatchesToHandler() {
        Product product = new Product();
        product.setType(ProductType.NORMAL);
        Order order = new Order();
        order.setId(1L);
        order.setItems(Set.of(product));
        when(orderPort.findById(1L)).thenReturn(Optional.of(order));

        Long result = service.processOrder(1L);

        assertEquals(1L, result);
        verify(handler).process(product);
    }

    @Test
    void processOrder_unknownOrder_throwsOrderNotFoundException() {
        when(orderPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.processOrder(99L));
    }

    @Test
    void processOrder_unsupportedType_throwsUnsupportedProductTypeException() {
        Product product = new Product();
        product.setType(ProductType.EXPIRABLE);
        Order order = new Order();
        order.setId(1L);
        order.setItems(Set.of(product));
        when(orderPort.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(UnsupportedProductTypeException.class, () -> service.processOrder(1L));
    }
}
