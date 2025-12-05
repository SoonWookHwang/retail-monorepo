package com.retail.payment.dto.response;

public record TossPaymentResponse(
    String paymentKey,
    String orderId,
    int totalAmount,
    String status
) {}
