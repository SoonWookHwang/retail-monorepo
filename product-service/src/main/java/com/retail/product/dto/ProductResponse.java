package com.retail.product.dto;

import com.retail.product.entity.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

  private Long id;
  private String name;
  private int price;
  private String description;
  private String brandName;
  private String categoryName;
  private int stockQuantity;
  private List<String> imageUrls;
  private List<String> reviewComments;

  public static ProductResponse from(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .description(product.getDescription())
        .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
        .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
        .stockQuantity(product.getStock() != null ? product.getStock().getQuantity() : 0)
        .imageUrls(product.getImages() != null
            ? product.getImages().stream().map(ProductImage::getImageUrl).collect(Collectors.toList())
            : List.of())
        .reviewComments(product.getReviews() != null
            ? product.getReviews().stream().map(Review::getComment).collect(Collectors.toList())
            : List.of())
        .build();
  }
}
