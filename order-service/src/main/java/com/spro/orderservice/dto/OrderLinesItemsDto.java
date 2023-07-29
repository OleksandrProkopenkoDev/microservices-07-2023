package com.spro.orderservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderLinesItemsDto(

         Long id,
         String skuCode,
         BigDecimal price,
         Integer quantity

) {
}
