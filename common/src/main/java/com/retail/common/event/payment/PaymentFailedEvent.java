package com.retail.common.event.payment;

import java.util.UUID;

public class PaymentFailedEvent {

  public static final String TOPIC = "payment-failed";

  private UUID orderId;
  private String reason;

  public PaymentFailedEvent(UUID orderId, String reason) {
    this.orderId = orderId;
    this.reason = reason;
  }

  public UUID getOrderId() { return orderId; }
  public String getReason() { return reason; }
}
