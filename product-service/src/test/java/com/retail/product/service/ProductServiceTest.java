package com.retail.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.entity.Brand;
import com.retail.product.entity.Category;
import com.retail.product.repository.BrandRepository;
import com.retail.product.repository.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

  @Autowired
  private ProductService productService;
  @Autowired
  private BrandRepository brandRepository;
  @Autowired
  private CategoryRepository categoryRepository;

  private Long brandId;
  private Long categoryId;

  @BeforeEach
  void setUp() {
    brandId = brandRepository.save(Brand.builder().name("Samsung").build()).getId();
    categoryId = categoryRepository.save(Category.builder().name("TV").build()).getId();
  }

  @Test
  @DisplayName("상품 등록 시 연관관계 엔티티가 Cascade로 함께 저장된다.")
  void createProduct() {
    ProductRequest req = ProductRequest.builder()
        .name("Neo QLED 8K")
        .price(2500000)
        .description("8K QLED TV with HDR")
        .brandId(brandId)
        .categoryId(categoryId)
        .stockQuantity(30)
        .imageUrls(List.of("https://example.com/tv-front.jpg"))
        .build();

    ProductResponse res = productService.create(req);

    System.out.println("=== Created Product ===");
    System.out.println("ID: " + res.getId());
    System.out.println("Name: " + res.getName());
    System.out.println("Brand: " + res.getBrandName());
    System.out.println("Category: " + res.getCategoryName());
    System.out.println("Price: " + res.getPrice());
    System.out.println("Images: " + res.getImageUrls());

    assertThat(res.getId()).isNotNull();
    assertThat(res.getBrandName()).isEqualTo("Samsung");
    assertThat(res.getCategoryName()).isEqualTo("TV");
    assertThat(res.getImageUrls()).hasSize(1);
  }

  @Test
  @DisplayName("상품 전체 조회 시 Fetch Join으로 브랜드/카테고리 정보가 함께 로딩된다.")
  void findAllProducts() {
    // given
    Long brandId = brandRepository.save(Brand.builder().name("LG").build()).getId();
    Long categoryId = categoryRepository.save(Category.builder().name("Monitor").build()).getId();

    productService.create(ProductRequest.builder()
        .name("UltraFine 5K")
        .price(1800000)
        .description("5K Retina Display Monitor")
        .brandId(brandId)
        .categoryId(categoryId)
        .stockQuantity(10)
        .imageUrls(List.of("https://example.com/monitor.jpg"))
        .build()
    );

    // when
    List<ProductResponse> list = productService.findAll();

    // then
    System.out.println("=== All Products ===");
    list.forEach(p -> System.out.printf(
        "ID=%d | Name=%s | Brand=%s | Category=%s | Price=%d%n",
        p.getId(), p.getName(), p.getBrandName(), p.getCategoryName(), p.getPrice()
    ));

    assertThat(list).isNotEmpty();
  }
}
