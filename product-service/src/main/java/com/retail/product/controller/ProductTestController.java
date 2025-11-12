package com.retail.product.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductTestController {

  @GetMapping("/test/success")
  public ApiResponse<String> successTest() {
    return ApiResponse.ok("common module 연동 성공!");
  }

  @GetMapping("/test/error")
  public ApiResponse<String> errorTest() {
    throw new CustomException(ErrorCode.INVALID_REQUEST);
  }
}
