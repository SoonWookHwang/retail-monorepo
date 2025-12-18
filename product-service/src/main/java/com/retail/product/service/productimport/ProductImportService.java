package com.retail.product.service.productimport;

import com.retail.product.dto.csv.ProductCsvDto;
import com.retail.product.entity.Brand;
import com.retail.product.entity.Category;
import com.retail.product.entity.Product;
import com.retail.product.entity.ProductImage;
import com.retail.product.repository.BrandRepository;
import com.retail.product.repository.CategoryRepository;
import com.retail.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImportService {

  private final BrandRepository brandRepository;
  private final ProductRepository productRepository;

  private final CategoryRepository categoryRepository;

  @Transactional
  public void importProducts(List<ProductCsvDto> dtos) {
    log.info("====== importProducts START, total {} rows ======", dtos.size());

    for (ProductCsvDto dto : dtos) {
      log.info("---- Processing Product: name={}, brand={}, category={}, parentCategory={} ----",
          dto.getProductName(), dto.getBrandName(), dto.getCategoryName(), dto.getParentCategoryName());

      // 1. 브랜드 처리
      Brand brand = null;
      if (dto.getBrandName() != null && !dto.getBrandName().isBlank()) {
        brand = brandRepository.findByName(dto.getBrandName())
            .orElseGet(() -> {
              log.info("Brand not found. Creating new brand: {}", dto.getBrandName());
              return brandRepository.save(Brand.builder().name(dto.getBrandName()).build());
            });
      }
      log.info("Brand resolved = {}", brand != null ? brand.getName() : "null");

      // 2. Parent category 처리
      Category parent = null;
      if (dto.getParentCategoryName() != null && !dto.getParentCategoryName().isBlank()) {
        parent = categoryRepository.findByName(dto.getParentCategoryName())
            .orElseThrow(() -> new IllegalArgumentException("부모 카테고리 없음: " + dto.getParentCategoryName()));
        log.info("Parent category resolved: id={}, name={}", parent.getId(), parent.getName());
      } else {
        log.info("Parent category is null or empty");
      }

      final Category finalParent = parent;

      // 3. 실제 카테고리 조회
      log.info("Searching category by name='{}' and parent='{}'",
          dto.getCategoryName(),
          finalParent != null ? finalParent.getName() : "null");

      Category category = categoryRepository.findByNameAndParent(dto.getCategoryName(), finalParent)
          .orElseGet(() -> {
            log.info("Category not found. Creating new category: name={}, parent={}",
                dto.getCategoryName(),
                finalParent != null ? finalParent.getName() : "null");

            return categoryRepository.save(
                Category.builder()
                    .name(dto.getCategoryName())
                    .parent(finalParent)
                    .build()
            );
          });

      log.info("Resolved Category: id={}, name={}, parent={}",
          category.getId(),
          category.getName(),
          category.getParent() != null ? category.getParent().getName() : "null");

      // 4. Product 생성
      Product product = Product.builder()
          .name(dto.getProductName())
          .price(dto.getPrice())
          .brand(brand)
          .category(category)
          .build();

      log.info("Created Product object: name={}, price={}, brand={}, category={}",
          product.getName(),
          product.getPrice(),
          product.getBrand() != null ? product.getBrand().getName() : "null",
          product.getCategory() != null ? product.getCategory().getName() : "null"
      );

      if(dto.getImageUrl()!=null&&!dto.getImageUrl().isBlank()){
        // 5. 이미지 저장
        ProductImage image = ProductImage.builder()
            .imageUrl(dto.getImageUrl())
            .isMain(true)
            .build();
        product.addImage(image);
      }
      productRepository.save(product);
      log.info("Saved Product: name={}, id assigned (after save) = {}", product.getName(), product.getId());
    }
    log.info("====== importProducts END ======");
  }
  @Transactional
  public void allProductsUpdate(){
    List<Product> all = productRepository.findAll();
    Category category = categoryRepository.findByName("TV").orElseThrow(()->new IllegalArgumentException("카테고리없음"));
    log.info("category name : {}",category.getName());
    all.forEach(product->{
      if(product.getCategory()==null){
        product.setCategory(category);
      }
    });
  }
}