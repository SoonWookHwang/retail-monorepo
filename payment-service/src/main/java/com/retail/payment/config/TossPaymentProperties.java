package com.retail.payment.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "toss")
public class TossPaymentProperties {

  private String secretKey;
  private String apiUrl;

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }
}
