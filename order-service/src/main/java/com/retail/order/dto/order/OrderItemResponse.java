package com.retail.order.dto.order;

import com.retail.order.entity.OrderItem;

public record OrderItemResponse(
    Long productId,
    String productName,
    int price,
    int quantity
) {
  public static OrderItemResponse from(OrderItem item) {
    return new OrderItemResponse(
        item.getProductId(),
        item.getProductName(),
        item.getPrice(),
        item.getQuantity()
    );
  }
}