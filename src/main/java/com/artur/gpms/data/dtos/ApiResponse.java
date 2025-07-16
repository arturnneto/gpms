package com.artur.gpms.data.dtos;

import java.util.List;

public record ApiResponse<T>(List<T> data, PaginationResponse pagination) {
}
