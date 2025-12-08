package com.retail.order.event.listener;

import com.retail.common.event.payment.PaymentFailedEvent;
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
public class PaymentFailedEventListener {
  private final OrderRepository orderRepository;

  @KafkaListener(topics = PaymentFailedEvent.TOPIC, groupId = "order-service", properties = {
      JsonDeserializer.VALUE_DEFAULT_TYPE + ":com.retail.common.event.payment.PaymentFailedEvent"
  })
  public void onPaymentFailed(PaymentFailedEvent event, Acknowledgment ack) {
    try {
      Order order = orderRepository.findById(event.getOrderId())
          .orElseThrow();
      order.markFailed();
      orderRepository.save(order);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Failed to process PaymentFailedEvent", e);
    }
  }
}
