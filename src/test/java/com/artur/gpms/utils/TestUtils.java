package com.artur.gpms.utils;

import com.artur.gpms.data.dtos.ItemEntityEvent;
import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.entities.ItemEntity;
import com.artur.gpms.data.entities.SaleOrderEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public TestUtils() {
    }

    public static List<ItemEntity> createItemList() {
        return List.of(
                ItemEntity.builder().product("Item 1").price(BigDecimal.valueOf(10)).quantity(10).description("Desc item 1").build(),
                ItemEntity.builder().product("Item 2").price(BigDecimal.valueOf(20)).quantity(20).description("Desc item 2").build(),
                ItemEntity.builder().product("Item 3").price(BigDecimal.valueOf(30)).quantity(30).description("Desc item 3").build()
        );
    }

    public static List<ItemEntityEvent> createItemEventList() {
        List<ItemEntityEvent> itemEntityEventList = new ArrayList<>();

        ItemEntityEvent itemEntityEventOne = new ItemEntityEvent("Product 1", 10, BigDecimal.valueOf(10), "Description 1");
        ItemEntityEvent itemEntityEventTwo = new ItemEntityEvent("Product 2", 20, BigDecimal.valueOf(20), "Description 2");
        ItemEntityEvent itemEntityEventThree = new ItemEntityEvent("Product 3", 30, BigDecimal.valueOf(30), "Description 3");

        itemEntityEventList.add(itemEntityEventOne);
        itemEntityEventList.add(itemEntityEventTwo);
        itemEntityEventList.add(itemEntityEventThree);

        return itemEntityEventList;
    }

    public static SaleOrderEntity createSaleOrder(String orderId, Long customerId, BigDecimal totalCost) {
        return SaleOrderEntity.builder().orderId(orderId).customerId(customerId).totalCost(totalCost).itemList(createItemList()).build();
    }

    public static SaleOrderEvent createSaleOrderEvent(Long customerId) {
        return new SaleOrderEvent(customerId, createItemEventList());
    }
}
