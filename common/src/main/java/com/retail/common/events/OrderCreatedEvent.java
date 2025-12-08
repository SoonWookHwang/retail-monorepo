package com.retail.common.events;

import java.util.List;

public record OrderCreatedEvent(
    Long orderId,
    Long userId,
    int totalPrice,
    List<OrderItemEvent> items
) {}


