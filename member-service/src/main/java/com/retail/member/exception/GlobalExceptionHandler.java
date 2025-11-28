package com.retail.member.exception;

import com.retail.common.dto.ApiResponse;
import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    return ResponseEntity
        .status(HttpStatus.valueOf(errorCode.getStatusCode()))
        .body(ApiResponse.fail(errorCode.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail("Unexpected error occurred"));
  }
}
