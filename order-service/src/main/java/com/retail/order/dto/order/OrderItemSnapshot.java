package com.retail.order.dto.order;

public record OrderItemSnapshot(
    Long productId,
    String productName,
    int price,
    int quantity
) {}