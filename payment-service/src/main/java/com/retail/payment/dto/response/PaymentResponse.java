package com.retail.payment.dto.response;

public record PaymentResponse(
    String orderId,
    int amount
) {}
