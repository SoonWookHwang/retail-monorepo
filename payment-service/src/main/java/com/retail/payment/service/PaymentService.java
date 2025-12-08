package com.retail.payment.service;

import com.retail.common.event.order.OrderCreatedEvent;
import com.retail.common.event.payment.PaymentApprovedEvent;
import com.retail.common.event.payment.PaymentFailedEvent;
import com.retail.payment.client.TossPaymentClient;
import com.retail.payment.dto.response.PaymentResponse;
import com.retail.payment.dto.response.TossPaymentResponse;
import com.retail.payment.entity.Payment;
import com.retail.payment.entity.PaymentStatus;
import com.retail.payment.event.producer.KafkaMessageProducer;
import com.retail.payment.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final TossPaymentClient tossPaymentClient;
  private final KafkaMessageProducer paymentEventProducer;


  /**
   * 결제 정보 조회 API
   */
  @Transactional(readOnly = true)
  public PaymentResponse getPaymentInfo(UUID orderId) {
    Payment p = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid orderId"));

    return new PaymentResponse(
        p.getOrderId(),
        p.getAmount(),
        p.getStatus()
    );
  }


  /**
   * 주문 생성 이벤트 수신 → 결제 대기 상태 생성
   */
  @Transactional
  public void createPendingPayment(OrderCreatedEvent event) {

    Payment payment = Payment.builder()
        .userId(event.getUserId())
        .orderId(event.getOrderId())
        .amount(event.getTotalPrice())
        .status(PaymentStatus.REQUESTED)
        .build();

    paymentRepository.save(payment);

    log.info("💰 Payment pending created: orderId={}, amount={}",
        event.getOrderId(), event.getTotalPrice());
  }


  /**
   * TossPayments 결제 승인 API (successUrl redirect 이후)
   */
  @Transactional
  public PaymentResponse confirmPayment(String paymentKey, UUID orderId, int amount) {

    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid orderId"));

    try {
      // 1) TossPayments 승인 요청
      TossPaymentResponse confirmResponse =
          tossPaymentClient.confirmPayment(paymentKey, orderId.toString(), amount);

      // 2) DB 업데이트
      payment.approve(confirmResponse.paymentKey());
      paymentRepository.save(payment);

      // 3) 결제 성공 이벤트 발행
      paymentEventProducer.send(
          PaymentApprovedEvent.TOPIC,
          new PaymentApprovedEvent(
              payment.getOrderId(),
              confirmResponse.totalAmount(),
              paymentKey
          )
      );

      log.info("Payment approved: orderId={}, paymentKey={}", orderId, paymentKey);

      return new PaymentResponse(
          orderId,
          confirmResponse.totalAmount(),
          payment.getStatus()
      );

    } catch (Exception e) {

      log.error("Payment approval failed for orderId={}", orderId, e);

      // 별도 트랜잭션으로 실패 처리
      markPaymentFailed(orderId, e.getMessage());

      throw new RuntimeException("Payment approval failed", e);
    }
  }


  /**
   * 결제 실패 처리 (별도 트랜잭션)
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markPaymentFailed(UUID orderId, String reason) {

    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow();

    payment.fail();
    paymentRepository.save(payment);

    // 결제 실패 이벤트 발행
    paymentEventProducer.send(
        PaymentFailedEvent.TOPIC,
        new PaymentFailedEvent(orderId, reason)
    );

    log.warn("Payment failed: orderId={}, reason={}", orderId, reason);
  }
}
