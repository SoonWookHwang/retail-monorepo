package com.retail.payment.dto.response;

import com.retail.payment.entity.PaymentStatus;
import java.util.UUID;

public record PaymentResponse(
    UUID orderId,
    int amount,
    PaymentStatus status
) {}
