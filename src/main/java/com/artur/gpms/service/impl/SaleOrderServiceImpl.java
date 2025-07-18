package com.artur.gpms.service.impl;

import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.entities.ItemEntity;
import com.artur.gpms.data.entities.SaleOrderEntity;
import com.artur.gpms.data.repositories.SaleOrderRepository;
import com.artur.gpms.service.SaleOrderService;
import jakarta.validation.ConstraintViolation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.artur.gpms.config.mq.RabbitMqConfig.SALES_ORDER_QUEUE;

@Service
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final Validator validator;

    public SaleOrderServiceImpl(SaleOrderRepository saleOrderRepository, MongoTemplate mongoTemplate, RabbitTemplate rabbitTemplate, Validator validator) {
        this.saleOrderRepository = saleOrderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.validator = validator;
    }

    public Optional<SaleOrderEntity> getSaleOrder(String orderId) {
        return saleOrderRepository.findByOrderId(orderId);
    }

    public void publishSaleOrderEvent(SaleOrderEvent saleOrderEvent) {
        rabbitTemplate.convertAndSend(SALES_ORDER_QUEUE, saleOrderEvent);
    }

    public SaleOrderEntity createNewSaleOrderEntity(SaleOrderEvent saleOrderEvent) {
        SaleOrderEntity saleOrderEntity = new SaleOrderEntity();
        saleOrderEntity.setCustomerId(saleOrderEvent.customerId());
        saleOrderEntity.setItemList(getSaleOrderItems(saleOrderEvent));
        saleOrderEntity.setTotalCost(getTotalOrderPrice(saleOrderEvent));

        return saleOrderEntity;
    }

    public void saveSaleOrderEvent(SaleOrderEvent saleOrderEvent) {
        SaleOrderEntity saleOrderEntity = createNewSaleOrderEntity(saleOrderEvent);
        saleOrderRepository.save(saleOrderEntity);
    }

    public List<ItemEntity> getSaleOrderItems(SaleOrderEvent saleOrderEvent) {
        return saleOrderEvent.itemList().stream()
                .map(i -> new ItemEntity(i.product(), i.quantity(), i.price(), i.description()))
                .toList();
    }

    public BigDecimal getTotalOrderPrice(SaleOrderEvent saleOrderEvent) {
        return saleOrderEvent.itemList().stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Page<SaleOrderEntity> findAllOrders(Pageable pageable) {
        return saleOrderRepository.findAll(pageable);
    }

    public void checkIfOrderExist(String orderId) {
        saleOrderRepository.findByOrderId(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

