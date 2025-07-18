package com.artur.gpms.data.dtos;

import com.artur.gpms.data.entities.ItemEntity;
import com.artur.gpms.data.entities.SaleOrderEntity;

import java.math.BigDecimal;
import java.util.List;

public record SaleOrderResponse(Long customerId,
                                BigDecimal total,
                                List<ItemEntity> itemList) {

    public static SaleOrderResponse fromEntity(SaleOrderEntity saleOrderEntity) {
        return new SaleOrderResponse(
                saleOrderEntity.getCustomerId(),
                saleOrderEntity.getTotalCost(),
                saleOrderEntity.getItemList()
        );
    }
}
