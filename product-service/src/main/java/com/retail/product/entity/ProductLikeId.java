package com.retail.product.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class ProductLikeId implements Serializable {
  private Long userId;
  private Long productId;
}