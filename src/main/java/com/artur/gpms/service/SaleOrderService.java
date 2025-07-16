package com.artur.gpms.service;

import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.entities.ItemEntity;
import com.artur.gpms.data.entities.SaleOrderEntity;
import com.artur.gpms.data.repositories.SaleOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;

    public SaleOrderService(SaleOrderRepository saleOrderRepository) {
        this.saleOrderRepository = saleOrderRepository;
    }

    public void saveSaleOrderEvent(SaleOrderEvent saleOrderEvent) {
        SaleOrderEntity saleOrderEntity = new SaleOrderEntity();

        saleOrderEntity.setOrderId(saleOrderEvent.orderId());
        saleOrderEntity.setCustomerId(saleOrderEntity.getCustomerId());
        saleOrderEntity.setItemList(getSaleOrderItems(saleOrderEvent));
        saleOrderEntity.setTotalCost(getTotalOrderPrice(saleOrderEvent));

        saleOrderRepository.save(saleOrderEntity);
    }

    private static List<ItemEntity> getSaleOrderItems(SaleOrderEvent saleOrderEvent) {
        return saleOrderEvent.itemList().stream()
                .map(i -> new ItemEntity(i.product(), i.quantity(), i.price(), i.description()))
                .toList();
    }

    private BigDecimal getTotalOrderPrice(SaleOrderEvent saleOrderEvent) {
        return saleOrderEvent.itemList().stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}

