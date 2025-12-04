package com.retail.order.dto.product;

public record ProductInfoResponse(
    Long id,
    String name,
    int price
) {}
