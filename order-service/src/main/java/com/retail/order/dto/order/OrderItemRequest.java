package com.retail.order.dto.order;

public record OrderItemRequest(
    Long productId,
    int quantity
) {}