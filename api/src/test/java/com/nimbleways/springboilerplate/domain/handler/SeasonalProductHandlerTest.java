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
class SeasonalProductHandlerTest {

    @Mock ProductPort productPort;
    @Mock NotificationPort notificationPort;
    SeasonalProductHandler handler;

    @BeforeEach void setUp() {
        handler = new SeasonalProductHandler(productPort, notificationPort);
    }

    @Test
    void supportedType_isSeasonal() {
        assertEquals(ProductType.SEASONAL, handler.supportedType());
    }

    @Test
    void inSeason_inStock_decrementsAndSaves() {
        Product p = seasonalProduct(10, LocalDate.now().minusDays(5), LocalDate.now().plusDays(30), 3);
        handler.process(p);
        assertEquals(9, p.getAvailable());
        verify(productPort).save(p);
        verifyNoInteractions(notificationPort);
    }

    @Test
    void leadTimeExceedsSeasonEnd_notifiesOutOfStockAndZeros() {
        Product p = seasonalProduct(0, LocalDate.now().minusDays(5), LocalDate.now().plusDays(3), 10);
        p.setName("EndSeason");
        handler.process(p);
        verify(notificationPort).sendOutOfStockNotification("EndSeason");
        assertEquals(0, p.getAvailable());
        verify(productPort).save(p);
    }

    @Test
    void preSeason_notifiesOutOfStock_noZero() {
        Product p = seasonalProduct(30, LocalDate.now().plusDays(30), LocalDate.now().plusDays(90), 5);
        p.setName("Grapes");
        handler.process(p);
        verify(notificationPort).sendOutOfStockNotification("Grapes");
        assertEquals(30, p.getAvailable());
    }

    @Test
    void inSeason_outOfStock_delayOk_notifiesDelay() {
        Product p = seasonalProduct(0, LocalDate.now().minusDays(10), LocalDate.now().plusDays(60), 5);
        p.setName("Mango");
        handler.process(p);
        verify(notificationPort).sendDelayNotification(5, "Mango");
    }

    private Product seasonalProduct(int available, LocalDate start, LocalDate end, int leadTime) {
        Product p = new Product();
        p.setType(ProductType.SEASONAL);
        p.setAvailable(available);
        p.setSeasonStartDate(start);
        p.setSeasonEndDate(end);
        p.setLeadTime(leadTime);
        p.setName("Seasonal");
        return p;
    }
}
