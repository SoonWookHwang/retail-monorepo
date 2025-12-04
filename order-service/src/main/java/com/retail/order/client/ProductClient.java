package com.retail.order.client;

import com.retail.order.dto.product.ProductInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProductClient {

  private final WebClient productWebClient;

  public ProductInfoResponse getProductInfo(Long id) {
    return productWebClient.get()
        .uri("/internal/products/{id}", id)
        .retrieve()
        .bodyToMono(ProductInfoResponse.class)
        .block(); // 동기 방식
  }

  public void decreaseStock(Long id, int quantity) {
    productWebClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/internal/products/{id}/decrease-stock")
            .queryParam("quantity", quantity)
            .build(id)
        )
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
