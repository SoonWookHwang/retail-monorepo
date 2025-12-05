package com.retail.payment.client;

import com.retail.payment.config.TossPaymentProperties;
import com.retail.payment.dto.request.TossConfirmRequest;
import com.retail.payment.dto.response.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class TossPaymentClient {

  private final WebClient webClient;
  private final TossPaymentProperties props;

  public TossPaymentResponse confirmPayment(String paymentKey, String orderId, int amount) {

    return webClient.post()
        .uri(props.getApiUrl() + "/confirm")
        .headers(headers -> {
          headers.setBasicAuth(props.getSecretKey(), "");
        })
        .bodyValue(new TossConfirmRequest(paymentKey, orderId, amount))
        .retrieve()
        .bodyToMono(TossPaymentResponse.class)
        .block();
  }
}
