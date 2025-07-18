package com.artur.gpms.service;

import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.entities.ItemEntity;
import com.artur.gpms.data.entities.SaleOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface SaleOrderService {


    Optional<SaleOrderEntity> getSaleOrder(String orderId);

    void publishSaleOrderEvent(SaleOrderEvent saleOrderEvent);

    SaleOrderEntity createNewSaleOrderEntity(SaleOrderEvent saleOrderEvent);

    void saveSaleOrderEvent(SaleOrderEvent saleOrderEvent);

    List<ItemEntity> getSaleOrderItems(SaleOrderEvent saleOrderEvent);

    BigDecimal getTotalOrderPrice(SaleOrderEvent saleOrderEvent);

    Page<SaleOrderEntity> findAllOrders(Pageable pageable);

    void checkIfOrderExist(String orderId);
}
