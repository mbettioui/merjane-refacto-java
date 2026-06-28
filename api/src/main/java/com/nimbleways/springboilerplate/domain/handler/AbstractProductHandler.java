package com.nimbleways.springboilerplate.domain.handler;

import com.nimbleways.springboilerplate.domain.model.Product;
import com.nimbleways.springboilerplate.domain.port.NotificationPort;
import com.nimbleways.springboilerplate.domain.port.ProductPort;

public abstract class AbstractProductHandler implements ProductHandler {

    protected final ProductPort productPort;
    protected final NotificationPort notificationPort;

    protected AbstractProductHandler(ProductPort productPort, NotificationPort notificationPort) {
        this.productPort = productPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public final void process(Product product) {
        if (isAvailable(product)) {
            decrementStock(product);
        } else {
            handleUnavailable(product);
        }
    }

    protected boolean isAvailable(Product p) {
        return p.getAvailable() != null && p.getAvailable() > 0;
    }

    protected void decrementStock(Product p) {
        p.setAvailable(p.getAvailable() - 1);
        productPort.save(p);
    }

    protected abstract void handleUnavailable(Product product);
}
