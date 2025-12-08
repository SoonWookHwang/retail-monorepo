package com.retail.payment.controller;

import com.retail.payment.dto.response.PaymentResponse;
import com.retail.payment.service.PaymentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;
  @GetMapping("/{orderId}")
  public PaymentResponse getPayment(@PathVariable UUID orderId) {
    return paymentService.getPaymentInfo(orderId);
  }

  @PostMapping("/confirm")
  public PaymentResponse confirmPayment(
      @RequestParam String paymentKey,
      @RequestParam UUID orderId,
      @RequestParam int amount
  ) {
    return paymentService.confirmPayment(paymentKey, orderId, amount);
  }

}
