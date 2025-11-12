package com.retail.common.exception;

import com.retail.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException ex) {
    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(ApiResponse.fail(ex.getErrorCode().getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
    return ResponseEntity
        .status(ErrorCode.INTERNAL_ERROR.getStatus())
        .body(ApiResponse.fail("Unexpected error occurred"));
  }
}
