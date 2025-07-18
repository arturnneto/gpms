package com.artur.gpms.controller;

import com.artur.gpms.data.dtos.PaginatedSaleOrderResponse;
import com.artur.gpms.data.dtos.SaleOrderResponse;
import com.artur.gpms.data.dtos.PaginationResponse;
import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.entities.SaleOrderEntity;
import com.artur.gpms.service.impl.SaleOrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SaleOrderController {

    private final SaleOrderServiceImpl saleOrderService;

    public SaleOrderController(SaleOrderServiceImpl saleOrderService) {
        this.saleOrderService = saleOrderService;
    }

    @GetMapping("/v1/orders/{orderId}")
    public ResponseEntity<SaleOrderResponse> getOrderByOrderId(@PathVariable("orderId") String orderId) {
        saleOrderService.checkIfOrderExist(orderId);
        Optional<SaleOrderEntity> saleOrderEntity = saleOrderService.getSaleOrder(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(SaleOrderResponse.fromEntity(saleOrderEntity.get()));
    }

    @PostMapping("/v1/orders")
    public ResponseEntity<SaleOrderEvent> createSaleOrder(@Valid @RequestBody SaleOrderEvent saleOrderEvent) {
        saleOrderService.publishSaleOrderEvent(saleOrderEvent);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saleOrderEvent);
    }

    @GetMapping("/v1/orders")
    public ResponseEntity<PaginatedSaleOrderResponse<SaleOrderResponse>> getAllSaleOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                          @RequestParam(name = "size", defaultValue = "10") Integer pageSize) {

        Page<SaleOrderEntity> pageEntity = saleOrderService.findAllOrders(PageRequest.of(page, pageSize));
        Page<SaleOrderResponse> pageResponse = pageEntity.map(SaleOrderResponse::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(new PaginatedSaleOrderResponse<>(
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
    }
}
