package com.retail.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value("${service.product.url}")
  private String productServiceUrl;

  @Bean
  public WebClient productWebClient() {
    return WebClient.builder()
        .baseUrl(productServiceUrl)
        .build();
  }
}
