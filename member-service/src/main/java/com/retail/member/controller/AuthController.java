package com.retail.member.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.member.dto.LoginRequest;
import com.retail.member.dto.SignupRequest;
import com.retail.member.dto.TokenResponse;
import com.retail.member.entity.Member;
import com.retail.member.service.AuthService;
import com.retail.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class AuthController {

  private final MemberService memberService;
  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<Member>> signup(@RequestBody @Valid SignupRequest request) {
    Member saved = memberService.signup(request);
    return ResponseEntity.ok(ApiResponse.ok(saved));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
    TokenResponse token = authService.login(request);
    return ResponseEntity.ok(ApiResponse.ok(token));
  }
}
