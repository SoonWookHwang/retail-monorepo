package com.retail.product.util;

import com.retail.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ErrorCodeUtils {

  public static HttpStatus toHttpStatus(ErrorCode errorCode) {
    try {
      return HttpStatus.valueOf(errorCode.getStatusCode());
    } catch (Exception e) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
