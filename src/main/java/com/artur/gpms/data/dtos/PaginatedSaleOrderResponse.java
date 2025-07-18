package com.artur.gpms.data.dtos;

import java.util.List;

public record PaginatedSaleOrderResponse<T>(List<T> data, PaginationResponse pagination) {
}
