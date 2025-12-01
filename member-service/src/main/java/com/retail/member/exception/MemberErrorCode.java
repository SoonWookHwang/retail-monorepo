package com.retail.member.exception;

import com.retail.common.exception.ErrorCode;

public enum MemberErrorCode {

  MEMBER_NOT_FOUND("MEMBER_001", ErrorCode.NOT_FOUND, "Member not found"),

  LOGIN_FAILED("MEMBER_002", ErrorCode.INVALID_REQUEST,
      "Invalid email or password"),

  DUPLICATE_EMAIL("MEMBER_003", ErrorCode.CONFLICT,
      "Email already exists");

  private final String code;
  private final ErrorCode base;
  private final String message;

  MemberErrorCode(String code, ErrorCode base, String message) {
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
