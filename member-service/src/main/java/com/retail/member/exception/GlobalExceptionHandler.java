package com.retail.member.exception;

import com.retail.common.dto.ApiResponse;
import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import com.retail.member.util.ErrorCodeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
    ErrorCode errorCode = ex.getErrorCode();

    return ResponseEntity
        .status(ErrorCodeUtils.toHttpStatus(errorCode))
        .body(ApiResponse.fail(errorCode));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {

    return ResponseEntity
        .status(ErrorCodeUtils.toHttpStatus(ErrorCode.INTERNAL_ERROR))
        .body(ApiResponse.fail(ErrorCode.INTERNAL_ERROR));
  }
}
