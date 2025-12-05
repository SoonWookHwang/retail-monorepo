package com.retail.payment.exception;

public class PaymentException extends RuntimeException {

  private final PaymentErrorCode errorCode;

  public PaymentException(PaymentErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public PaymentErrorCode getErrorCode() {
    return errorCode;
  }
}
