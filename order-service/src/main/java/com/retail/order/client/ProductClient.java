package com.retail.order.client;

import com.retail.order.dto.product.ProductInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "${service.product.url}")
public interface ProductClient {

  @GetMapping("/internal/products/{id}")
  ProductInfoResponse getProductInfo(@PathVariable Long id);

  @PostMapping("/internal/products/{id}/decrease-stock")
  void decreaseStock(@PathVariable Long id, @RequestParam int quantity);
}