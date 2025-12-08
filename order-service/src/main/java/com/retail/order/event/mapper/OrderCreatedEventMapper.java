package com.retail.order.event.mapper;

import com.retail.common.event.order.OrderCreatedEvent;
import com.retail.common.event.order.OrderItemEvent;
import com.retail.order.entity.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreatedEventMapper {

  public static OrderCreatedEvent from(Order order) {

    int totalPrice = order.getItems().stream()
        .mapToInt(item -> item.getPrice() * item.getQuantity())
        .sum();

    List<OrderItemEvent> items = order.getItems().stream()
        .map(item -> new OrderItemEvent(
            item.getProductId(),
            item.getProductName(),
            item.getPrice(),
            item.getQuantity()
        ))
        .collect(Collectors.toList());

    return new OrderCreatedEvent(
        order.getId(),
        order.getUserId(),
        totalPrice,
        items
    );
  }
}
