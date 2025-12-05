package com.retail.payment.dto.request;

public record PaymentConfirmRequest(
    String paymentKey,
    String orderId,
    int amount,
    Long userId
) {}
