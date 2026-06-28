package com.nimbleways.springboilerplate.infra.config;

import com.nimbleways.springboilerplate.api.ProcessOrderUseCase;
import com.nimbleways.springboilerplate.domain.ProcessOrderService;
import com.nimbleways.springboilerplate.domain.handler.ExpirableProductHandler;
import com.nimbleways.springboilerplate.domain.handler.NormalProductHandler;
import com.nimbleways.springboilerplate.domain.handler.ProductHandler;
import com.nimbleways.springboilerplate.domain.handler.SeasonalProductHandler;
import com.nimbleways.springboilerplate.domain.port.NotificationPort;
import com.nimbleways.springboilerplate.domain.port.OrderPort;
import com.nimbleways.springboilerplate.domain.port.ProductPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    NormalProductHandler normalHandler(ProductPort p, NotificationPort n) {
        return new NormalProductHandler(p, n);
    }

    @Bean
    SeasonalProductHandler seasonalHandler(ProductPort p, NotificationPort n) {
        return new SeasonalProductHandler(p, n);
    }

    @Bean
    ExpirableProductHandler expirableHandler(ProductPort p, NotificationPort n) {
        return new ExpirableProductHandler(p, n);
    }

    @Bean
    ProcessOrderUseCase processOrderUseCase(OrderPort orderPort, List<ProductHandler> handlers) {
        return new ProcessOrderService(orderPort, handlers);
    }
}
