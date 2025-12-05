package com.retail.payment.exception;

import com.retail.common.exception.ErrorCode;

public enum PaymentErrorCode {

  INVALID_AMOUNT("PAY_001", ErrorCode.INVALID_REQUEST, "Invalid payment amount"),
  PAYMENT_FAILED("PAY_002", ErrorCode.INVALID_REQUEST, "Payment approval failed");

  private final String code;
  private final ErrorCode base;
  private final String message;

  PaymentErrorCode(String code, ErrorCode base, String message) {
    this.code = code;
    this.base = base;
    this.message = message;
  }

  public PaymentException toException() {
    return new PaymentException(this);
  }

  public String getMessage() { return message; }

  public ErrorCode getBase() { return base; }

  public String getCode() { return code; }
}
