package com.artur.gpms.data.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record SaleOrderEvent(
        @NotNull @Positive Long customerId,
        @NotNull @Valid List<ItemEntityEvent> itemList
        ) {
}
