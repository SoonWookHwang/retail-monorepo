package com.retail.product.config;

import com.retail.product.document.ProductDocument;
import com.retail.product.entity.*;
import com.retail.product.repository.*;
import com.retail.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("docker")
public class ProductDummyDataLoader implements CommandLineRunner {

  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final ProductSearchService productSearchService;

  private final Faker faker = new Faker(Locale.KOREA);
  private final Random random = new Random();

  @Override
  public void run(String... args) {

    if (productRepository.count() > 0) {
      log.info("Dummy data already exists. Skipping initialization.");
      return;
    }

    log.info("Initializing dummy product data…");

    // 브랜드 생성
    Brand nike = brandRepository.save(Brand.builder().name("Nike").build());
    Brand adidas = brandRepository.save(Brand.builder().name("Adidas").build());
    Brand apple = brandRepository.save(Brand.builder().name("Apple").build());

    // 상위 카테고리
    Category shoes = categoryRepository.save(Category.builder().name("신발").build());
    Category electronics = categoryRepository.save(Category.builder().name("전자기기").build());
    Category clothes = categoryRepository.save(Category.builder().name("의류").build());

    // 하위 카테고리
    List<Category> allCategories = List.of(
        shoes,
        electronics,
        clothes,

        categoryRepository.save(Category.builder().name("운동화").parent(shoes).build()),
        categoryRepository.save(Category.builder().name("축구화").parent(shoes).build()),
        categoryRepository.save(Category.builder().name("샌들").parent(shoes).build()),

        categoryRepository.save(Category.builder().name("스마트폰").parent(electronics).build()),
        categoryRepository.save(Category.builder().name("노트북").parent(electronics).build()),
        categoryRepository.save(Category.builder().name("태블릿").parent(electronics).build()),

        categoryRepository.save(Category.builder().name("상의").parent(clothes).build()),
        categoryRepository.save(Category.builder().name("하의").parent(clothes).build()),
        categoryRepository.save(Category.builder().name("아우터").parent(clothes).build())
    );

    List<Brand> brands = List.of(nike, adidas, apple);

    // 상품 생성 + ES 인덱싱
    for (int i = 0; i < 100; i++) {

      Brand brand = brands.get(random.nextInt(brands.size()));
      Category category = allCategories.get(random.nextInt(allCategories.size()));

      Product product = Product.builder()
          .name(faker.commerce().productName())
          .description(faker.lorem().sentence(10))
          .price(random.nextInt(20) * 10000 + 10000)
          .brand(brand)
          .category(category)
          .build();

      ProductStock stock = ProductStock.builder()
          .quantity(random.nextInt(50) + 1)
          .build();

      product.setStock(stock);

      for (int img = 0; img < 3; img++) {
        product.addImage(ProductImage.builder()
            .imageUrl("https://placehold.co/600x600?text=Product+" + (i + 1))
            .isMain(img == 0)
            .build());
      }
      // 리뷰 1~3개 랜덤 생성
//      int reviewCount = random.nextInt(3) + 1;
//      for (int r = 0; r < reviewCount; r++) {
//        product.addReview(Review.builder()
//            .rating(random.nextInt(5) + 1)
//            .reviewerName(faker.name().fullName())
//            .comment(faker.lorem().sentence(15))
//            .build());
//      }
      Product saved = productRepository.save(product);

      ProductDocument doc = ProductDocument.fromEntity(saved, 0);
      productSearchService.index(doc);
    }

    log.info("Dummy products + Elasticsearch indexing completed!");
  }
}