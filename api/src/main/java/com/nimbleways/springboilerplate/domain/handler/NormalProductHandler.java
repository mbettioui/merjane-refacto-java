package com.nimbleways.springboilerplate.domain.handler;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.model.ProductType;
import com.nimbleways.springboilerplate.domain.port.NotificationPort;
import com.nimbleways.springboilerplate.domain.port.ProductPort;

public class NormalProductHandler extends AbstractProductHandler {

    public NormalProductHandler(ProductPort productPort, NotificationPort notificationPort) {
        super(productPort, notificationPort);
    }

    @Override
    public ProductType supportedType() {
        return ProductType.NORMAL;
    }

    @Override
    protected void handleUnavailable(Product p) {
        if (p.getLeadTime() != null && p.getLeadTime() > 0) {
            notificationPort.sendDelayNotification(p.getLeadTime(), p.getName());
        }
    }
}
