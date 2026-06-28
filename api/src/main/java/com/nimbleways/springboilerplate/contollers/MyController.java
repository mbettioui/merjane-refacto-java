package com.nimbleways.springboilerplate.contollers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;

import java.time.LocalDate;
import java.util.Set;

import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class MyController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @PostMapping("{orderId}/processOrder")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        log.debug("Processing order {}", order);
        Set<Product> products = order.getItems();
        for (Product p : products) {
            switch (ProductType.from(p.getType())) {
                case NORMAL -> {
                    if (p.getAvailable() > 0) {
                        p.setAvailable(p.getAvailable() - 1);
                        productRepository.save(p);
                    } else {
                        int leadTime = p.getLeadTime();
                        if (leadTime > 0) {
                            productService.notifyDelay(leadTime, p);
                        }
                    }
                }
                case SEASONAL -> {
                    if (LocalDate.now().isAfter(p.getSeasonStartDate())
                            && LocalDate.now().isBefore(p.getSeasonEndDate())
                            && p.getAvailable() > 0) {
                        p.setAvailable(p.getAvailable() - 1);
                        productRepository.save(p);
                    } else {
                        productService.handleSeasonalProduct(p);
                    }
                }
                case EXPIRABLE -> {
                    if (p.getAvailable() > 0 && p.getExpiryDate().isAfter(LocalDate.now())) {
                        p.setAvailable(p.getAvailable() - 1);
                        productRepository.save(p);
                    } else {
                        productService.handleExpiredProduct(p);
                    }
                }
            }
        }

        return new ProcessOrderResponse(order.getId());
    }
}
