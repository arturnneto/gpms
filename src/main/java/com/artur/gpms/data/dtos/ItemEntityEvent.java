package com.artur.gpms.data.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemEntityEvent(
        @NotBlank String product,
        @NotNull @Positive Integer quantity,
        @NotNull @Positive BigDecimal price,
        @NotBlank String description
        ) {
}
