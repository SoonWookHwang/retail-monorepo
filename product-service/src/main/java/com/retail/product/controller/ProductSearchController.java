package com.retail.product.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.product.document.ProductDocument;
import com.retail.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/search")
@RequiredArgsConstructor
public class ProductSearchController {

  private final ProductSearchService productSearchService;

  @GetMapping
  public ApiResponse<List<ProductDocument>> search(@RequestParam String keyword) {
    return ApiResponse.ok(productSearchService.search(keyword));
  }

  @GetMapping("/boost")
  public ApiResponse<List<ProductDocument>> searchWithBoost(@RequestParam String keyword) {
    return ApiResponse.ok(productSearchService.searchWithBoost(keyword));
  }
}
