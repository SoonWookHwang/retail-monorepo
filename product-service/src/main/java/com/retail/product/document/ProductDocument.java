package com.retail.product.document;

import com.retail.product.entity.Product;
import com.retail.product.entity.ProductImage;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")
public class ProductDocument {

  @Id
  private Long id;

  private String name;
  private String description;
  private int price;

  private String brandName;
  private String categoryName;

  private List<String> imageUrls;

  private int likeCount;

  public static ProductDocument fromEntity(Product product, int likeCount) {
    return ProductDocument.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .brandName(product.getBrand().getName())
        .categoryName(product.getCategory().getName())
        .imageUrls(product.getImages().stream().map(ProductImage::getImageUrl).toList())
        .likeCount(likeCount)
        .build();
  }
}
