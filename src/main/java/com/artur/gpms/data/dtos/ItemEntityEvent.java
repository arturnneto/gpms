package com.artur.gpms.data.dtos;

import java.math.BigDecimal;

public record ItemEntityEvent(
        String product,
        Integer quantity,
        BigDecimal price,
        String description
        ) {
}
