package com.retail.gateway.exception;

import com.retail.common.exception.ErrorCode;

public enum GatewayErrorCode {
  AUTH_FAILED("GATEWAY_401", ErrorCode.UNAUTHORIZED, "Unauthorized or invalid token");

  private final String code;
  private final ErrorCode base;
  private final String message;

  GatewayErrorCode(String code, ErrorCode base, String message) {
    this.code = code;
    this.base = base;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public ErrorCode getBase() {
    return base;
  }

  public String getMessage() {
    return message;
  }
}
