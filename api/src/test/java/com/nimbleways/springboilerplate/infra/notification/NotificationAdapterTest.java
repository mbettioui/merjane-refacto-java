package com.nimbleways.springboilerplate.infra.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationAdapterTest {

    @Mock  NotificationService notificationService;
    @InjectMocks NotificationAdapter adapter;

    @Test
    void sendDelayNotification_delegates() {
        adapter.sendDelayNotification(10, "USB Cable");
        verify(notificationService).sendDelayNotification(10, "USB Cable");
    }

    @Test
    void sendOutOfStockNotification_delegates() {
        adapter.sendOutOfStockNotification("Watermelon");
        verify(notificationService).sendOutOfStockNotification("Watermelon");
    }

    @Test
    void sendExpirationNotification_delegates() {
        LocalDate date = LocalDate.now();
        adapter.sendExpirationNotification("Milk", date);
        verify(notificationService).sendExpirationNotification("Milk", date);
    }
}
