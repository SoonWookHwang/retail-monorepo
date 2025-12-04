package com.retail.product.service;

import com.retail.product.dto.internal.ProductInfoResponse;
import com.retail.product.entity.Product;
import com.retail.product.entity.ProductStock;
import com.retail.product.exception.ProductErrorCode;
import com.retail.product.repository.ProductRepository;
import com.retail.product.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalProductService {

  private final ProductRepository productRepository;
  private final ProductStockRepository productStockRepository;

  @Transactional(readOnly = true)
  public ProductInfoResponse getProductInfo(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(ProductErrorCode.PRODUCT_NOT_FOUND::toException);

    return new ProductInfoResponse(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getStock().getQuantity()
    );
  }

  @Transactional
  public void decreaseStock(Long productId, int quantity) {
    ProductStock stock = productStockRepository.findForUpdate(productId)
        .orElseThrow(ProductErrorCode.STOCK_NOT_FOUND::toException);

    if (stock.getQuantity() < quantity) {
      throw ProductErrorCode.OUT_OF_STOCK.toException();
    }

    stock.setQuantity(stock.getQuantity() - quantity);
  }
}

