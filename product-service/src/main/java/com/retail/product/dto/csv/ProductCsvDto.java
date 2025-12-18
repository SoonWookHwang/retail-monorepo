package com.retail.product.dto.csv;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductCsvDto {
  private String brandName;
  private String productName;
  private Integer price;
  private String imageUrl;
  private String categoryName;
  private String parentCategoryName;
}