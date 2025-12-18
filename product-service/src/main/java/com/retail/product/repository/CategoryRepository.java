package com.retail.product.repository;

import com.retail.product.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByNameAndParent(String name, Category parent);
  Optional<Category> findByName(String name);
}
