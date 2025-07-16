package com.artur.gpms.controller;

import com.artur.gpms.data.dtos.ApiResponse;
import com.artur.gpms.data.dtos.OrderResponse;
import com.artur.gpms.data.dtos.PaginationResponse;
import com.artur.gpms.service.SaleOrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleOrderController {

    private final SaleOrderService saleOrderService;

    public SaleOrderController(SaleOrderService saleOrderService) {
        this.saleOrderService = saleOrderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrdersByCustomer(@PathVariable("customerId") Long customerId,
                                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                           @RequestParam(name = "page", defaultValue = "10") Integer pageSize) {

        var pageResponse = saleOrderService.findAllOrdersByCustomerId(customerId, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(new ApiResponse<>(
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
    }
}
