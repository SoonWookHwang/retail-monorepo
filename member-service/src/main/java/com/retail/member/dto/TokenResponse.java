package com.retail.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  public TokenResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
