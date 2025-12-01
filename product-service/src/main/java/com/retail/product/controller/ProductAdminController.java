package com.retail.product.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.service.ProductAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/admin")
@RequiredArgsConstructor
public class ProductAdminController {

  private final ProductAdminService productAdminService;

  @PostMapping
  public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
    return ApiResponse.ok(productAdminService.create(request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    productAdminService.delete(id);
    return ApiResponse.ok(null);
  }

}
