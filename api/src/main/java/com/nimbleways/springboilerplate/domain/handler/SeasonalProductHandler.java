package com.nimbleways.springboilerplate.domain.handler;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.port.NotificationPort;
import com.nimbleways.springboilerplate.domain.port.ProductPort;

import java.time.LocalDate;

public class SeasonalProductHandler extends AbstractProductHandler {

    public SeasonalProductHandler(ProductPort productPort, NotificationPort notificationPort) {
        super(productPort, notificationPort);
    }

    @Override
    public ProductType supportedType() {
        return ProductType.SEASONAL;
    }

    @Override
    protected boolean isAvailable(Product p) {
        return super.isAvailable(p)
                && LocalDate.now().isAfter(p.getSeasonStartDate())
                && LocalDate.now().isBefore(p.getSeasonEndDate());
    }

    @Override
    protected void handleUnavailable(Product p) {
        if (LocalDate.now().plusDays(p.getLeadTime()).isAfter(p.getSeasonEndDate())) {
            notificationPort.sendOutOfStockNotification(p.getName());
            p.setAvailable(0);
            productPort.save(p);
        } else if (p.getSeasonStartDate().isAfter(LocalDate.now())) {
            notificationPort.sendOutOfStockNotification(p.getName());
            productPort.save(p);
        } else {
            productPort.save(p);
            notificationPort.sendDelayNotification(p.getLeadTime(), p.getName());
        }
    }
}
