package com.retail.order.event.listener;

import com.retail.common.event.payment.PaymentApprovedEvent;
import com.retail.order.entity.Order;
import com.retail.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentApprovedEventListener {

  private final OrderRepository orderRepository;

  @KafkaListener(topics = PaymentApprovedEvent.TOPIC, groupId = "order-service", properties = {
      JsonDeserializer.VALUE_DEFAULT_TYPE + ":com.retail.common.event.payment.PaymentApprovedEvent"
  })
  public void onPaymentApproved(PaymentApprovedEvent event, Acknowledgment ack) {
    try {
      log.info("Received PaymentApprovedEvent: {}", event);
      Order order = orderRepository.findById(event.getOrderId())
          .orElseThrow();
      order.markPaid();
      orderRepository.save(order);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Failed to process PaymentApprovedEvent", e);
    }
  }
}
