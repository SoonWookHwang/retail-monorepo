package com.retail.product.controller.internal;

import com.retail.product.dto.internal.ProductInfoResponse;
import com.retail.product.service.InternalProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/products")
public class InternalProductController {

  private final InternalProductService internalProductService;

  @GetMapping("/{id}")
  public ProductInfoResponse getProductInfo(@PathVariable Long id) {
    return internalProductService.getProductInfo(id);
  }

  @PostMapping("/{id}/decrease-stock")
  public void decreaseStock(
      @PathVariable Long id,
      @RequestParam int quantity
  ) {
    internalProductService.decreaseStock(id, quantity);
  }
}
