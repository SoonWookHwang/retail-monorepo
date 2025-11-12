package com.retail.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.retail.product.entity.Brand;
import com.retail.product.entity.Category;
import com.retail.product.entity.Product;
import com.retail.product.entity.ProductImage;
import com.retail.product.entity.ProductStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  @DisplayName("상품 저장 시 연관 엔티티(브랜드, 카테고리, 이미지, 재고)가 함께 저장된다.")
  void saveProductWithRelations() {
    // given
    Brand brand = brandRepository.save(Brand.builder().name("Apple").build());
    Category category = categoryRepository.save(Category.builder().name("Smartphone").build());

    Product product = Product.builder()
        .name("iPhone 16 Pro")
        .price(1900000)
        .description("A17 Pro chip, 6.7 inch")
        .brand(brand)
        .category(category)
        .build();

    product.setStock(ProductStock.builder().quantity(50).build());
    product.addImage(ProductImage.builder().imageUrl("https://example.com/iphone.jpg").isMain(true).build());
    product.addImage(ProductImage.builder().imageUrl("https://example.com/iphone2.jpg").isMain(true).build());
    product.addImage(ProductImage.builder().imageUrl("https://example.com/iphone3.jpg").isMain(true).build());

    // when
    Product saved = productRepository.save(product);

    System.out.println("=== Saved Product Info ===");
    System.out.println("Product ID: " + saved.getId());
    System.out.println("Name: " + saved.getName());
    System.out.println("Price: " + saved.getPrice());
    System.out.println("Brand: " + saved.getBrand().getName());
    System.out.println("Category: " + saved.getCategory().getName());
    System.out.println("Stock: " + saved.getStock().getQuantity());
    System.out.println("Images: " + saved.getImages().stream().map(ProductImage::getImageUrl).toList());
    // then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getStock()).isNotNull();
    assertThat(saved.getImages()).hasSize(3);
    assertThat(saved.getBrand().getName()).isEqualTo("Apple");
  }
}
