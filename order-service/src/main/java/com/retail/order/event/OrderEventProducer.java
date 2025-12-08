package com.retail.order.event;

import com.retail.common.event.order.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void publishOrderCreated(OrderCreatedEvent event) {
    kafkaTemplate.send("order-created", event);
  }
}

