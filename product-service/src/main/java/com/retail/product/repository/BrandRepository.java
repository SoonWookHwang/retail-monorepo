package com.retail.product.repository;

import com.retail.product.entity.Brand;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

  boolean existsByName(String brandName);

  Optional<Brand> findByName(String brandName);
}
