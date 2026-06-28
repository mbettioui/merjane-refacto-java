package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MyControllerIntegrationTests {

    @Autowired private MockMvc mockMvc;
    @MockBean  private NotificationService notificationService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;

    // ------------------------------------------------------------------ NORMAL

    @Test
    void processOrderShouldDecrementStockForNormalProduct() throws Exception {
        Product p = savedProduct(new Product(null, 15, 30, "NORMAL", "USB Cable", null, null, null));
        postOrder(p);
        assertEquals(29, reload(p).getAvailable());
        verifyNoInteractions(notificationService);
    }

    @Test
    void processOrderShouldNotifyDelayForOutOfStockNormalProduct() throws Exception {
        Product p = savedProduct(new Product(null, 15, 0, "NORMAL", "USB Dongle Out", null, null, null));
        postOrder(p);
        verify(notificationService, times(1)).sendDelayNotification(15, "USB Dongle Out");
    }

    @Test
    void processOrderShouldDoNothingForOutOfStockNormalProductWithNoLeadTime() throws Exception {
        Product p = savedProduct(new Product(null, 0, 0, "NORMAL", "Gadget No Lead", null, null, null));
        postOrder(p);
        verifyNoInteractions(notificationService);
    }

    // --------------------------------------------------------------- EXPIRABLE

    @Test
    void processOrderShouldDecrementStockForValidExpirableProduct() throws Exception {
        Product p = savedProduct(new Product(null, 15, 5, "EXPIRABLE", "Fresh Butter",
                LocalDate.now().plusDays(10), null, null));
        postOrder(p);
        assertEquals(4, reload(p).getAvailable());
        verifyNoInteractions(notificationService);
    }

    @Test
    void processOrderShouldNotifyExpirationForExpiredProduct() throws Exception {
        LocalDate expiry = LocalDate.now().minusDays(2);
        Product p = savedProduct(new Product(null, 90, 6, "EXPIRABLE", "Expired Milk", expiry, null, null));
        postOrder(p);
        verify(notificationService, times(1)).sendExpirationNotification("Expired Milk", expiry);
        assertEquals(0, reload(p).getAvailable());
    }

    // ---------------------------------------------------------------- SEASONAL

    @Test
    void processOrderShouldDecrementStockForInSeasonProduct() throws Exception {
        Product p = savedProduct(new Product(null, 5, 10, "SEASONAL", "Watermelon",
                null, LocalDate.now().minusDays(10), LocalDate.now().plusDays(60)));
        postOrder(p);
        assertEquals(9, reload(p).getAvailable());
        verifyNoInteractions(notificationService);
    }

    @Test
    void processOrderShouldNotifyOutOfStockWhenLeadTimeExceedsSeasonEnd() throws Exception {
        Product p = savedProduct(new Product(null, 10, 0, "SEASONAL", "EndOfSeason",
                null, LocalDate.now().minusDays(5), LocalDate.now().plusDays(5)));
        postOrder(p);
        verify(notificationService, times(1)).sendOutOfStockNotification("EndOfSeason");
        assertEquals(0, reload(p).getAvailable());
    }

    @Test
    void processOrderShouldNotifyOutOfStockForPreSeasonProduct() throws Exception {
        Product p = savedProduct(new Product(null, 15, 30, "SEASONAL", "Grapes PreSeason",
                null, LocalDate.now().plusDays(180), LocalDate.now().plusDays(240)));
        postOrder(p);
        verify(notificationService, times(1)).sendOutOfStockNotification("Grapes PreSeason");
        assertEquals(30, reload(p).getAvailable());
    }

    @Test
    void processOrderShouldNotifyDelayForInSeasonOutOfStockSeasonalProduct() throws Exception {
        Product p = savedProduct(new Product(null, 5, 0, "SEASONAL", "InSeason NoStock",
                null, LocalDate.now().minusDays(10), LocalDate.now().plusDays(60)));
        postOrder(p);
        verify(notificationService, times(1)).sendDelayNotification(5, "InSeason NoStock");
    }

    // ----------------------------------------------------------------- helpers

    @Test
    void processOrder_unknownOrder_returns404() throws Exception {
        mockMvc.perform(post("/orders/{orderId}/processOrder", 999999L)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    private Product savedProduct(Product p) {
        return productRepository.save(p);
    }

    private Product reload(Product p) {
        return productRepository.findById(p.getId()).orElseThrow();
    }

    private void postOrder(Product... products) throws Exception {
        Order order = new Order();
        order.setItems(Set.of(products));
        Long orderId = orderRepository.save(order).getId();
        mockMvc.perform(post("/orders/{orderId}/processOrder", orderId)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
