package com.retail.payment.dto.request;

public record TossConfirmRequest(
    String paymentKey,
    String orderId,
    int amount
) {}
