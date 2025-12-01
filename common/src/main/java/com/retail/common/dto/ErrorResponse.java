package com.retail.common.dto;

import com.retail.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private int statusCode;
  private String statusName;
  private String message;

  private String domainCode;

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(
        errorCode.getStatusCode(),
        errorCode.getStatusName(),
        errorCode.getMessage(),
        null
    );
  }

  public static ErrorResponse from(ErrorCode errorCode, String domainCode) {
    return new ErrorResponse(
        errorCode.getStatusCode(),
        errorCode.getStatusName(),
        errorCode.getMessage(),
        domainCode
    );
  }
}
