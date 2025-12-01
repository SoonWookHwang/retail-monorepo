package com.retail.common.exception;

public enum ErrorCode {
  INVALID_REQUEST(400, "BAD_REQUEST", "Invalid request"),
  NOT_FOUND(404, "NOT_FOUND", "Resource not found"),
  CONFLICT(409, "CONFLICT", "Conflict"),
  INTERNAL_ERROR(500, "INTERNAL_SERVER_ERROR", "Internal server error");

  private final int statusCode;
  private final String statusName;
  private final String message;

  ErrorCode(int statusCode, String statusName, String message) {
    this.statusCode = statusCode;
    this.statusName = statusName;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getStatusName() {
    return statusName;
  }

  public String getMessage() {
    return message;
  }
}
