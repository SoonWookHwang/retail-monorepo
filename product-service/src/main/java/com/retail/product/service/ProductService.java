package com.retail.product.service;

import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.entity.*;
import com.retail.product.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;

  public ProductResponse create(ProductRequest request) {
    Brand brand = brandRepository.findById(request.getBrandId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid brand id"));
    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));

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
    return ProductResponse.from(saved);
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAllWithBrandAndCategory().stream()
        .map(ProductResponse::from)
        .collect(Collectors.toList());
  }

  public ProductResponse findById(Long id) {
    return productRepository.findById(id)
        .map(ProductResponse::from)
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }
}
