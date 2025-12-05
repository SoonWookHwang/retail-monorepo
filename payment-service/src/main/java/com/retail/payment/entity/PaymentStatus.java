package com.retail.payment.entity;

public enum PaymentStatus {

  /**
   * 결제가 시작되었지만 아직 사용자 결제 완료 전
   */
  REQUESTED,

  /**
   * Toss 결제 승인 성공
   */
  APPROVED,

  /**
   * Toss 결제 승인 실패 or 오류
   */
  FAILED,

  /**
   * 결제 취소 처리됨
   */
  CANCELLED
}
