package com.retail.payment.controller;

import com.retail.common.resolver.CurrentUser;
import com.retail.payment.dto.request.PaymentRequest;
import com.retail.payment.dto.response.PaymentResponse;
import com.retail.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @GetMapping("/confirm")
  public PaymentResponse confirmPayment(
      @RequestParam String paymentKey,
      @RequestParam String orderId,
      @RequestParam int amount
  ) {
    return paymentService.confirmPayment(paymentKey, orderId, amount);
  }
}
