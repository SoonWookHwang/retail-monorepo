package com.retail.order.service;

import com.retail.order.client.ProductClient;
import com.retail.order.dto.order.OrderCreateRequest;
import com.retail.order.dto.order.OrderItemSnapshot;
import com.retail.order.dto.order.OrderResponse;
import com.retail.order.dto.order.OrderSnapshotRequest;
import com.retail.order.dto.product.ProductInfoResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacadeService {

  private final ProductClient productClient;
  private final OrderService orderService;

  public OrderResponse createOrder(Long userId, OrderCreateRequest request) {

    List<OrderItemSnapshot> snapshots = new ArrayList<>();
    int totalPrice = 0;

    // 1) 상품 가격 조회 & 스냅샷 생성
    for (var item : request.items()) {
      ProductInfoResponse product = productClient.getProductInfo(item.productId());

      snapshots.add(
          new OrderItemSnapshot(
              product.id(),
              product.name(),
              product.price(),
              item.quantity()
          )
      );

      totalPrice += product.price() * item.quantity();
    }

    // 2) 결제 개발 필요

    // 3) 재고 차감
    for (var snap : snapshots) {
      productClient.decreaseStock(snap.productId(), snap.quantity());
    }

    // 4) 주문 저장
    return orderService.createOrder(userId, new OrderSnapshotRequest(snapshots));
  }
}

