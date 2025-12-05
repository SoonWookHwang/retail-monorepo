package com.retail.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/payments/webhook")
public class PaymentWebhookController {

  @PostMapping
  public ResponseEntity<Void> receiveWebhook(@RequestBody String payload) {
    log.info("📌 Toss Webhook received: {}", payload);

    // TODO: signature 검증 추가

    return ResponseEntity.ok().build();
  }
}
