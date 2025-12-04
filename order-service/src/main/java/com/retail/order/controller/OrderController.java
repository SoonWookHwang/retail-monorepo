package com.retail.order.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.common.resolver.CurrentUser;
import com.retail.order.dto.order.OrderCreateRequest;
import com.retail.order.dto.order.OrderResponse;
import com.retail.order.service.OrderFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderFacadeService orderFacadeService;


  @PostMapping
  public ApiResponse<OrderResponse> createOrder(
      @CurrentUser Long userId,
      @RequestBody OrderCreateRequest request
  ) {
    OrderResponse orderNo = orderFacadeService.createOrder(userId, request);
    return ApiResponse.ok(orderNo);
  }
}