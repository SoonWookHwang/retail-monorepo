package com.retail.payment.config;

import java.util.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient tossWebClient() {
    return WebClient.builder()
        .defaultHeader(HttpHeaders.AUTHORIZATION,
            "Basic " + Base64.getEncoder()
                .encodeToString((System.getenv("TOSS_SECRET_KEY") + ":").getBytes()))
        .build();
  }
}
