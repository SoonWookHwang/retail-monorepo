package com.retail.common.dto;

import com.retail.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private int statusCode;
  private String statusName;
  private String message;

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(
        errorCode.getStatusCode(),
        errorCode.getStatusName(),
        errorCode.getMessage()
    );
  }
}
