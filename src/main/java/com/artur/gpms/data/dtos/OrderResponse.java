package com.artur.gpms.data.dtos;

import com.artur.gpms.data.entities.SaleOrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customerId,
                            BigDecimal total) {

    public static OrderResponse fromEntity(SaleOrderEntity saleOrderEntity) {
        return new OrderResponse(saleOrderEntity.getOrderId(),saleOrderEntity.getCustomerId(), saleOrderEntity.getTotalCost());
    }
}
