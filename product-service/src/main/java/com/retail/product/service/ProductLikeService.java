package com.retail.product.service;

import com.retail.product.entity.ProductLike;
import com.retail.product.exception.ProductErrorCode;
import com.retail.product.repository.ProductLikeRepository;
import com.retail.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

  private final ProductLikeRepository productLikeRepository;
  private final ProductRepository productRepository;

  @Transactional
  public boolean toggleLike(Long userId, Long productId) {

    productRepository.findById(productId)
        .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);

    boolean exists = productLikeRepository.existsByUserIdAndProductId(userId, productId);

    if (exists) {
      productLikeRepository.deleteByUserIdAndProductId(userId, productId);
      return false;
    } else {
      productLikeRepository.save(new ProductLike(userId, productId));
      return true;
    }
  }
}
