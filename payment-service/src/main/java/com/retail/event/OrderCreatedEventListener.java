package com.retail.payment.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.common.event.order.OrderCreatedEvent;
import com.retail.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener {

  private final ObjectMapper objectMapper;
  private final PaymentService paymentService;

  @KafkaListener(topics = "order-created", groupId = "payment-service")
  public void onOrderCreated(String message) {
    try {
      OrderCreatedEvent event =
          objectMapper.readValue(message, OrderCreatedEvent.class);

      log.info("📩 Kafka Received OrderCreatedEvent: {}", event);

      // 1) 주문 생성됨 → 결제 준비 단계 생성
      paymentService.createPendingPayment(event);

    } catch (Exception e) {
      log.error("❌ Failed to process OrderCreatedEvent", e);
    }
  }
}
