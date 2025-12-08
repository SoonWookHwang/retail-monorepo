package com.retail.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity {

  @Id
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

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

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
  }

  public void addItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);
  }

  public void markPaid() {
    this.status = OrderStatus.PAID;
  }

  public void markFailed() {
    this.status = OrderStatus.FAILED;
  }
}
