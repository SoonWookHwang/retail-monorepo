package com.retail.common.events;

public record OrderItemEvent(
    Long productId,
    int quantity,
    int price
) {}