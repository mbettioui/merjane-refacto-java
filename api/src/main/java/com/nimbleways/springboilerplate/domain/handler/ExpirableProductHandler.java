package com.nimbleways.springboilerplate.domain.handler;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.port.NotificationPort;
import com.nimbleways.springboilerplate.domain.port.ProductPort;

import java.time.LocalDate;

public class ExpirableProductHandler extends AbstractProductHandler {

    public ExpirableProductHandler(ProductPort productPort, NotificationPort notificationPort) {
        super(productPort, notificationPort);
    }

    @Override
    public ProductType supportedType() {
        return ProductType.EXPIRABLE;
    }

    @Override
    protected boolean isAvailable(Product p) {
        return super.isAvailable(p) && p.getExpiryDate().isAfter(LocalDate.now());
    }

    @Override
    protected void handleUnavailable(Product p) {
        notificationPort.sendExpirationNotification(p.getName(), p.getExpiryDate());
        p.setAvailable(0);
        productPort.save(p);
    }
}
