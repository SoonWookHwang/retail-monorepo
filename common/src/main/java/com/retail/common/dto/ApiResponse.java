package com.retail.common.dto;

import com.retail.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
  private boolean success;
  private T data;

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(true, data);
  }

  public static ApiResponse<ErrorResponse> fail(ErrorCode errorCode) {
    return new ApiResponse<>(
        false,
        ErrorResponse.from(errorCode)
    );
  }
  public static ApiResponse<ErrorResponse> fail(ErrorCode errorCode, String domainCode) {
    return new ApiResponse<>(
        false,
        ErrorResponse.from(errorCode, domainCode)
    );
  }

}
