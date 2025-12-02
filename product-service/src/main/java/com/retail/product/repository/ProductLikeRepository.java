package com.retail.product.repository;

import com.retail.product.entity.ProductLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

  boolean existsByUserIdAndProductId(Long userId, Long productId);

  void deleteByUserIdAndProductId(Long userId, Long productId);

  List<ProductLike> findByUserIdAndProductIdIn(Long userId, List<Long> productIds);

  int countByProductId(Long productId);

}

