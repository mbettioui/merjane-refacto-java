package com.nimbleways.springboilerplate.domain.handler;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.port.NotificationPort;
import com.nimbleways.springboilerplate.domain.port.ProductPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ExpirableProductHandlerTest {

    @Mock ProductPort productPort;
    @Mock NotificationPort notificationPort;
    ExpirableProductHandler handler;

    @BeforeEach void setUp() {
        handler = new ExpirableProductHandler(productPort, notificationPort);
    }

    @Test
    void supportedType_isExpirable() {
        assertEquals(ProductType.EXPIRABLE, handler.supportedType());
    }

    @Test
    void inStock_notExpired_decrementsAndSaves() {
        Product p = expirableProduct(5, LocalDate.now().plusDays(10));
        handler.process(p);
        assertEquals(4, p.getAvailable());
        verify(productPort).save(p);
        verifyNoInteractions(notificationPort);
    }

    @Test
    void expired_notifiesAndZerosStock() {
        LocalDate expiry = LocalDate.now().minusDays(1);
        Product p = expirableProduct(3, expiry);
        p.setName("Milk");
        handler.process(p);
        verify(notificationPort).sendExpirationNotification("Milk", expiry);
        assertEquals(0, p.getAvailable());
        verify(productPort).save(p);
    }

    @Test
    void outOfStock_notExpired_notifiesExpiration() {
        LocalDate expiry = LocalDate.now().plusDays(5);
        Product p = expirableProduct(0, expiry);
        p.setName("Butter");
        handler.process(p);
        verify(notificationPort).sendExpirationNotification("Butter", expiry);
        assertEquals(0, p.getAvailable());
    }

    private Product expirableProduct(int available, LocalDate expiryDate) {
        Product p = new Product();
        p.setType(ProductType.EXPIRABLE);
        p.setAvailable(available);
        p.setExpiryDate(expiryDate);
        p.setName("Item");
        return p;
    }
}
