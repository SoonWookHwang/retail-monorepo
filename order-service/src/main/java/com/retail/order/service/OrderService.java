package com.retail.order.service;

import com.retail.common.event.order.OrderCreatedEvent;
import com.retail.order.dto.order.OrderResponse;
import com.retail.order.dto.order.OrderSnapshotRequest;
import com.retail.order.entity.Order;
import com.retail.order.entity.OrderItem;
import com.retail.order.entity.OrderStatus;
import com.retail.order.event.mapper.OrderCreatedEventMapper;
import com.retail.order.event.producer.KafkaMessageProducer;
import com.retail.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final KafkaMessageProducer orderEventProducer;

  @Transactional
  public OrderResponse createOrder(Long userId, OrderSnapshotRequest request) {

    Order order = Order.builder()
        .userId(userId)
        .status(OrderStatus.CREATED)
        .totalAmount(
            request.items()
                .stream()
                .mapToInt(i -> i.price() * i.quantity())
                .sum()
        )
        .build();

    for (var snap : request.items()) {
      OrderItem item = OrderItem.builder()
          .productId(snap.productId())
          .productName(snap.productName())
          .price(snap.price())
          .quantity(snap.quantity())
          .build();

      order.addItem(item);
    }

    Order saved = orderRepository.save(order);

    OrderCreatedEvent event = OrderCreatedEventMapper.from(saved);
    orderEventProducer.send(OrderCreatedEvent.TOPIC, event);

    return OrderResponse.from(saved);
  }
}
