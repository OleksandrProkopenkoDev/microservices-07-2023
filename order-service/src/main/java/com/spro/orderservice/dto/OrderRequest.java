package com.spro.orderservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(
        List<OrderLinesItemsDto> orderLinesItemsDtoList
) {
}
