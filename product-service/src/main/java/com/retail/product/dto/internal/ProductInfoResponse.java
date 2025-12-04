package com.retail.product.dto.internal;

public record ProductInfoResponse(
    Long id,
    String name,
    int price,
    int stockQuantity
) {}