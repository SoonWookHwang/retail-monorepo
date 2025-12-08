package com.retail.common.event.payment;

import java.util.UUID;

public class PaymentApprovedEvent {
  public static final String TOPIC = "payment-approved";
  private final UUID orderId;
  private final int amount;
  private final String paymentKey;

  public PaymentApprovedEvent(UUID orderId, int amount, String paymentKey) {
    this.orderId = orderId;
    this.amount = amount;
    this.paymentKey = paymentKey;
  }

  public UUID getOrderId() { return orderId; }
  public int getAmount() { return amount; }
  public String getPaymentKey() { return paymentKey; }
}
