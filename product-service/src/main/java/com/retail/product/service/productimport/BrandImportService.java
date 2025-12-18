package com.retail.product.service.productimport;

import com.retail.product.entity.Brand;
import com.retail.product.repository.BrandRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandImportService {

  private final BrandRepository brandRepository;

  public void importBrands(List<String> brandNames) {
    Set<String> unique = new HashSet<>(brandNames);

    for (String name : unique) {
      if (!brandRepository.existsByName(name)) {
        brandRepository.save(Brand.builder().name(name).build());
      }
    }
  }
}
