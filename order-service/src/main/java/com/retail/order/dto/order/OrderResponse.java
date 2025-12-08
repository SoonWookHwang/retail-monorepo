package com.retail.order.dto.order;

import com.retail.order.entity.Order;
import com.retail.order.entity.OrderStatus;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID orderId,
    Long userId,
    int totalAmount,
    OrderStatus status,
    List<OrderItemResponse> items
) {

  public static OrderResponse from(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getUserId(),
        order.getTotalAmount(),
        order.getStatus(),
        order.getItems().stream().map(OrderItemResponse::from).toList()
    );
  }
}