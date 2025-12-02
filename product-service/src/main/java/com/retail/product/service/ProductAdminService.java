package com.retail.product.service;

import com.retail.product.document.ProductDocument;
import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.entity.Brand;
import com.retail.product.entity.Category;
import com.retail.product.entity.Product;
import com.retail.product.entity.ProductImage;
import com.retail.product.entity.ProductStock;
import com.retail.product.exception.ProductErrorCode;
import com.retail.product.exception.ProductException;
import com.retail.product.repository.BrandRepository;
import com.retail.product.repository.CategoryRepository;
import com.retail.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductAdminService {

  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;

  private final ProductSearchService productSearchService;

  @Transactional
  public ProductResponse create(ProductRequest request) {
    Brand brand = brandRepository.findById(request.getBrandId())
        .orElseThrow(() -> new ProductException(ProductErrorCode.INVALID_BRAND));
    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new ProductException(ProductErrorCode.INVALID_CATEGORY));

    Product product = Product.builder()
        .name(request.getName())
        .price(request.getPrice())
        .description(request.getDescription())
        .brand(brand)
        .category(category)
        .build();

    ProductStock stock = ProductStock.builder()
        .quantity(request.getStockQuantity())
        .build();

    product.setStock(stock);

    request.getImageUrls().forEach(url ->
        product.addImage(ProductImage.builder()
            .imageUrl(url)
            .isMain(false)
            .build())
    );

    Product saved = productRepository.save(product);
    productSearchService.index(
        ProductDocument.fromEntity(saved, 0)
    );
    return ProductResponse.from(saved);
  }

  @Transactional
  public void delete(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

    productRepository.delete(product);
  }
}
