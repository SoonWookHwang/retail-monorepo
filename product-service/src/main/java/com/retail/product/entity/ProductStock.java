package com.retail.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductStock {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int quantity;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  public void setProduct(Product product) {
    this.product = product;
  }
}
