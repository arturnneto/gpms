package com.artur.gpms.config.mq;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String SALES_ORDER_QUEUE = "sale-order-created";
}
