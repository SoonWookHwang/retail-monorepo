package com.retail.payment.dto.request;

public record PaymentRequest(
    int amount,
    String orderName
) {}
