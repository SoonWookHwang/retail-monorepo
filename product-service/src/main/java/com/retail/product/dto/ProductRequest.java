package com.retail.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

  private String name;
  private int price;
  private String description;

  private Long brandId;
  private Long categoryId;

  private int stockQuantity;

  // 여러 이미지 URL 입력 가능
  private List<String> imageUrls;
}
