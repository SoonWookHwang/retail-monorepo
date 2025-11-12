package com.retail.product.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
    return ApiResponse.ok(productService.create(request));
  }

  @GetMapping
  public ApiResponse<List<ProductResponse>> getAll() {
    return ApiResponse.ok(productService.findAll());
  }

  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
    return ApiResponse.ok(productService.findById(id));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ApiResponse.ok(null);
  }
}
