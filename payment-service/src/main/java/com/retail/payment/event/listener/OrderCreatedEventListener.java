package com.retail.payment.event.listener;

import com.retail.common.event.order.OrderCreatedEvent;
import com.retail.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener {
  private final PaymentService paymentService;

  @KafkaListener(topics = OrderCreatedEvent.TOPIC, groupId = "payment-service", properties = {
      JsonDeserializer.VALUE_DEFAULT_TYPE + ":com.retail.common.event.order.OrderCreatedEvent"
  })
  public void onOrderCreated(OrderCreatedEvent event, Acknowledgment ack) {
    try {
      log.info("Kafka Received OrderCreatedEvent: {}", event);
      // 1) 주문 생성됨 → 결제 준비 단계 생성
      paymentService.createPendingPayment(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Failed to process OrderCreatedEvent", e);
    }
  }
}
