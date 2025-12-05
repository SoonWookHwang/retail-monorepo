package com.retail.payment.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  @Column(nullable = false)
  private String orderId;
  @Column(nullable = false)
  private int amount;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  private String paymentKey;

  @Builder
  public Payment(Long userId, String orderId, int amount, PaymentStatus status, String paymentKey) {
    this.userId = userId;
    this.orderId = orderId;
    this.amount = amount;
    this.status = status;
    this.paymentKey = paymentKey;
  }

  public void approve(String paymentKey) {
    this.paymentKey = paymentKey;
    this.status = PaymentStatus.APPROVED;
  }

  public void fail() {
    this.status = PaymentStatus.FAILED;
  }
}



