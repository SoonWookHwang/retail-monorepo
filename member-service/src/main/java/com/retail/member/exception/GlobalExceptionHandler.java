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
  public ResponseEntity<ApiResponse<?>> handleCommonException(CustomException ex) {
    ErrorCode baseError = ex.getErrorCode();

    return ResponseEntity
        .status(ErrorCodeUtils.toHttpStatus(baseError))
        .body(ApiResponse.fail(baseError));
  }

  @ExceptionHandler(MemberException.class)
  public ResponseEntity<ApiResponse<?>> handleMemberException(MemberException ex) {

    ErrorCode baseError = ex.getErrorCode().getBase();
    String domainCode = ex.getErrorCode().getCode();

    return ResponseEntity
        .status(ErrorCodeUtils.toHttpStatus(baseError))
        .body(ApiResponse.fail(baseError, domainCode));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
    return ResponseEntity
        .status(ErrorCodeUtils.toHttpStatus(ErrorCode.INTERNAL_ERROR))
        .body(ApiResponse.fail(ErrorCode.INTERNAL_ERROR));
  }
}
