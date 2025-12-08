package com.retail.payment.service;

import com.retail.common.event.order.OrderCreatedEvent;
import com.retail.payment.client.TossPaymentClient;
import com.retail.payment.dto.request.PaymentRequest;
import com.retail.payment.dto.response.PaymentResponse;
import com.retail.payment.dto.response.TossPaymentResponse;
import com.retail.payment.entity.Payment;
import com.retail.payment.entity.PaymentStatus;
import com.retail.payment.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final TossPaymentClient tossPaymentClient;

  /**
   *  주문이 생성되면 OrderCreatedEvent를 받고
   *  결제 대기(PENDING/REQUESTED) 상태로 Payment 레코드를 만든다.
   */
  @Transactional
  public void createPendingPayment(OrderCreatedEvent event) {

    Payment payment = Payment.builder()
        .userId(event.userId())
        .orderId(String.valueOf(event.orderId()))   // 주문번호 그대로 사용
        .amount(event.totalPrice())
        .status(PaymentStatus.REQUESTED)
        .build();

    paymentRepository.save(payment);

    log.info("💰 Payment pending created: orderId={}, amount={}",
        event.orderId(), event.totalPrice());
  }


  /**
   *  사용자 결제 성공 후 Toss에서 successUrl로 redirect될 때 호출되는 승인 처리
   */
  @Transactional
  public PaymentResponse confirmPayment(String paymentKey, String orderId, int amount) {

    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid orderId"));

    // 1) Toss에 승인 요청
    TossPaymentResponse confirmResponse =
        tossPaymentClient.confirmPayment(paymentKey, orderId, amount);

    // 2) 승인 성공 시 DB 업데이트
    payment.approve(confirmResponse.paymentKey());
    paymentRepository.save(payment);

    log.info("✔️ Payment approved: orderId={}, paymentKey={}", orderId, paymentKey);

    return new PaymentResponse(orderId, confirmResponse.totalAmount());
  }

}
