package com.retail.product.repository;

import com.retail.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("SELECT DISTINCT p FROM Product p " +
      "JOIN FETCH p.brand b " +
      "JOIN FETCH p.category c")
  List<Product> findAllWithBrandAndCategory();
}
