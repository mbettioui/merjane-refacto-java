package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.api.ProcessOrderUseCase;
import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductTypeException;
import com.nimbleways.springboilerplate.domain.handler.ProductHandler;
import com.nimbleways.springboilerplate.domain.model.Order;
import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.port.OrderPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class ProcessOrderService implements ProcessOrderUseCase {

    private final OrderPort orderPort;
    private final Map<ProductType, ProductHandler> handlersByType;

    public ProcessOrderService(OrderPort orderPort, List<ProductHandler> handlers) {
        this.orderPort = orderPort;
        this.handlersByType = handlers.stream()
                .collect(toMap(ProductHandler::supportedType, identity()));
    }

    @Override
    public Long processOrder(Long orderId) {
        Order order = orderPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        for (Product product : order.getItems()) {
            handlerOf(product.getType()).process(product);
        }
        return order.getId();
    }

    private ProductHandler handlerOf(ProductType type) {
        return Optional.ofNullable(handlersByType.get(type))
                .orElseThrow(() -> new UnsupportedProductTypeException(type));
    }
}
