package com.retail.product.repository;

import com.retail.product.entity.ProductStock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select ps from ProductStock ps where ps.product.id = :productId")
  Optional<ProductStock> findForUpdate(Long productId);

}
