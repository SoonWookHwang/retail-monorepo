package com.retail.product.exception;

import com.retail.common.exception.ErrorCode;

public enum ProductErrorCode {

  PRODUCT_NOT_FOUND("PRODUCT_001", ErrorCode.NOT_FOUND, "Product not found"),
  INVALID_PRODUCT_STATUS("PRODUCT_002", ErrorCode.INVALID_REQUEST, "Invalid product status"),
  OUT_OF_STOCK("PRODUCT_003", ErrorCode.INVALID_REQUEST, "Product out of stock"),
  INVALID_BRAND("PRODUCT_004",ErrorCode.INVALID_REQUEST,"Invalid brand id"),
  INVALID_CATEGORY("PRODUCT_005",ErrorCode.INVALID_REQUEST,"Invalid category id"),
  STOCK_NOT_FOUND("PRODUCT_006", ErrorCode.NOT_FOUND, "Stock not found");


  private final String code;
  private final ErrorCode base;
  private final String message;

  ProductErrorCode(String code, ErrorCode base, String message) {
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

  public ProductException toException() {
    return new ProductException(this);
  }
}
