package com.retail.product.controller;

import com.retail.product.document.ProductDocument;
import com.retail.product.entity.Product;
import com.retail.product.repository.ProductLikeRepository;
import com.retail.product.repository.ProductRepository;
import com.retail.product.service.ProductSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductReindexController {

  private final ProductRepository productRepository;
  private final ProductLikeRepository productLikeRepository;
  private final ProductSearchService productSearchService;

  @PostMapping("/reindex")
  public String reindexAll() {

    List<Product> products = productRepository.findAll();

    for (Product p : products) {
      int likeCount = productLikeRepository.countByProductId(p.getId());
      productSearchService.index(ProductDocument.fromEntity(p, likeCount));
    }

    return "Reindexed " + products.size() + " products.";
  }
}