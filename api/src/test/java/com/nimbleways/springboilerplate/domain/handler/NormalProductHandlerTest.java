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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class NormalProductHandlerTest {

    @Mock ProductPort productPort;
    @Mock NotificationPort notificationPort;
    NormalProductHandler handler;

    @BeforeEach void setUp() {
        handler = new NormalProductHandler(productPort, notificationPort);
    }

    @Test
    void supportedType_isNormal() {
        assertEquals(ProductType.NORMAL, handler.supportedType());
    }

    @Test
    void inStock_decrementsAndSaves() {
        Product p = product(10);
        handler.process(p);
        assertEquals(9, p.getAvailable());
        verify(productPort).save(p);
        verifyNoInteractions(notificationPort);
    }

    @Test
    void outOfStock_withLeadTime_notifiesDelay() {
        Product p = product(0);
        p.setLeadTime(15);
        p.setName("RJ45");
        handler.process(p);
        verify(notificationPort).sendDelayNotification(15, "RJ45");
        verify(productPort, never()).save(any());
    }

    @Test
    void outOfStock_noLeadTime_doesNothing() {
        Product p = product(0);
        p.setLeadTime(0);
        handler.process(p);
        verifyNoInteractions(notificationPort);
        verify(productPort, never()).save(any());
    }

    private Product product(int available) {
        Product p = new Product();
        p.setType(ProductType.NORMAL);
        p.setAvailable(available);
        p.setLeadTime(0);
        p.setName("Cable");
        return p;
    }
}
