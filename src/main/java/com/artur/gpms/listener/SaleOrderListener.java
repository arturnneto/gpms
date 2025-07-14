package com.artur.gpms.listener;


import com.artur.gpms.data.dtos.SaleOrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.artur.gpms.config.mq.RabbitMqConfig.SALES_ORDER_QUEUE;

@Component
public class SaleOrderListener {

    @RabbitListener(queues = SALES_ORDER_QUEUE)
    public void listen(Message<SaleOrderEvent> message) {

    }
}
