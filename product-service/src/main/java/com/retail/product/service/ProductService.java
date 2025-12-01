package com.retail.product.service;

import com.retail.product.dto.ProductResponse;
import com.retail.product.entity.ProductLike;
import com.retail.product.exception.ProductErrorCode;
import com.retail.product.exception.ProductException;
import com.retail.product.repository.ProductLikeRepository;
import com.retail.product.repository.ProductRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductLikeRepository productLikeRepository;

  @Transactional(readOnly = true)
  public List<ProductResponse> findAll(Long userId) {

    var products = productRepository.findAllWithBrandAndCategory()
        .stream()
        .map(ProductResponse::from)
        .collect(Collectors.toList());

    return mergeLikeStatus(products, userId);
  }

  @Transactional(readOnly = true)
  public ProductResponse findById(Long productId, Long userId) {
    var product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    if (userId == null) {
      return ProductResponse.from(product, false);
    }
    boolean liked = productLikeRepository.existsByUserIdAndProductId(userId, productId);
    return ProductResponse.from(product, liked);
  }


  public List<ProductResponse> mergeLikeStatus(List<ProductResponse> products, Long userId) {

    if (userId == null) {
      products.forEach(p -> p.setLiked(false));
      return products;
    }

    List<Long> productIds = products.stream()
        .map(ProductResponse::getId)
        .toList();

    Set<Long> likedIds = productLikeRepository
        .findByUserIdAndProductIdIn(userId, productIds)
        .stream()
        .map(ProductLike::getProductId)
        .collect(Collectors.toSet());

    products.forEach(p -> p.setLiked(likedIds.contains(p.getId())));

    return products;
  }

}
