package com.retail.common.event.order;

import java.util.List;
import java.util.UUID;

public class OrderCreatedEvent {

  public static final String TOPIC = "order-created";

  private UUID orderId;
  private Long userId;
  private int totalPrice;
  private List<OrderItemEvent> items;

  public OrderCreatedEvent(UUID orderId, Long userId, int totalPrice, List<OrderItemEvent> items) {
    this.orderId = orderId;
    this.userId = userId;
    this.totalPrice = totalPrice;
    this.items = items;
  }

  public UUID getOrderId() { return orderId; }
  public Long getUserId() { return userId; }
  public int getTotalPrice() { return totalPrice; }
  public List<OrderItemEvent> getItems() { return items; }
}
