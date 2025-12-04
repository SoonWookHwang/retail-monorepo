package com.retail.order.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId; // 주문자

  private int totalAmount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(
      mappedBy = "order",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @Builder.Default
  private List<OrderItem> items = new ArrayList<>();

  public void addItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);
  }

  public void updateStatus(OrderStatus status) {
    this.status = status;
  }
}
