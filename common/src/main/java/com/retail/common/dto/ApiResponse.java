package com.retail.common.dto;

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

  public static <T> ApiResponse<T> fail(T data) {
    return new ApiResponse<>(false, data);
  }
}
