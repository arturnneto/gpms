package com.artur.gpms.listener;

import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.service.SaleOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.artur.gpms.config.mq.RabbitMqConfig.SALES_ORDER_QUEUE;

@Component
public class SaleOrderListener {

    private final Logger logger = LoggerFactory.getLogger(SaleOrderListener.class);
    private final SaleOrderService saleOrderService;

    public SaleOrderListener(SaleOrderService saleOrderService) {
        this.saleOrderService = saleOrderService;
    }

    @RabbitListener(queues = SALES_ORDER_QUEUE)
    public void listen(Message<SaleOrderEvent> message) {
        logger.info("Message consumed: {}", message);

        saleOrderService.saveSaleOrderEvent(message.getPayload());
    }
}
