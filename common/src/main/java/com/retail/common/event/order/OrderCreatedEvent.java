package com.retail.common.event.order;

import java.util.List;

public record OrderCreatedEvent(
    Long orderId,
    Long userId,
    int totalPrice,
    List<OrderItemEvent> items
) {}


