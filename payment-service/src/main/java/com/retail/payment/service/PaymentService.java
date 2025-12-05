package com.retail.payment.service;

import com.retail.payment.client.TossPaymentClient;
import com.retail.payment.dto.request.PaymentRequest;
import com.retail.payment.dto.response.PaymentResponse;
import com.retail.payment.dto.response.TossPaymentResponse;
import com.retail.payment.entity.Payment;
import com.retail.payment.entity.PaymentStatus;
import com.retail.payment.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final TossPaymentClient tossPaymentClient;

  /**
   * 1) 결제 요청 생성
   */
  @Transactional
  public PaymentResponse requestPayment(Long userId, PaymentRequest req) {
    String orderId = "order_" + UUID.randomUUID();
    Payment payment = Payment.builder()
        .userId(userId) // null 허용
        .orderId(orderId)
        .amount(req.amount())
        .status(PaymentStatus.REQUESTED)
        .build();
    paymentRepository.save(payment);

    return new PaymentResponse(
        payment.getOrderId(),
        payment.getAmount()
    );
  }

  /**
   * 2) 결제 승인 (Toss 측에서 리다이렉트 이후)
   */
  @Transactional
  public PaymentResponse confirmPayment(String paymentKey, String orderId, int amount) {

    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid orderId"));

    TossPaymentResponse confirmResponse =
        tossPaymentClient.confirmPayment(paymentKey, orderId, amount);

    payment.approve(confirmResponse.paymentKey());
    paymentRepository.save(payment);

    return new PaymentResponse(
        orderId,
        confirmResponse.totalAmount()
    );
  }
}
