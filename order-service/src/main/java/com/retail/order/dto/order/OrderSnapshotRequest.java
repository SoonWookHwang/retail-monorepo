package com.retail.order.dto.order;

import java.util.List;

public record OrderSnapshotRequest(
    List<OrderItemSnapshot> items
) {}