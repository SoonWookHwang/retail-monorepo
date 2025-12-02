package com.retail.product.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.common.resolver.CurrentUser;
import com.retail.product.dto.ProductResponse;
import com.retail.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ApiResponse<List<ProductResponse>> getAll(@CurrentUser Long userId) {
    return ApiResponse.ok(productService.findAll(userId));
  }

  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getById(@PathVariable Long id,@CurrentUser Long userId) {
    return ApiResponse.ok(productService.findById(id,userId));
  }


}
