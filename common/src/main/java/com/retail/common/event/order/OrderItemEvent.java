package com.retail.common.event.order;

public record OrderItemEvent(
    Long productId,
    String productName,
    int quantity,
    int price
) {}