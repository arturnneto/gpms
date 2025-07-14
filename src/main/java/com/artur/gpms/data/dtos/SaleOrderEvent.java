package com.artur.gpms.data.dtos;

import com.artur.gpms.data.entities.ItemEntity;

import java.math.BigDecimal;
import java.util.List;

public record SaleOrderEvent(
        Long orderId,
        Long customerId,
        BigDecimal totalCost,
        List<ItemEntityEvent> itemList
        ) {
}
