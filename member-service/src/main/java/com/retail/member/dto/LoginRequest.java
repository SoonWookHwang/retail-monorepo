package com.retail.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class LoginRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
