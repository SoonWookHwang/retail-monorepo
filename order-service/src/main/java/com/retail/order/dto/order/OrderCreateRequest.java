package com.retail.order.dto.order;

import java.util.List;

public record OrderCreateRequest(
    List<OrderItemRequest> items
) {}