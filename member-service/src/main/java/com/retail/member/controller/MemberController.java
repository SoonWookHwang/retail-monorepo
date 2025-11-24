package com.retail.member.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.member.dto.MemberResponseDto;
import com.retail.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<MemberResponseDto>> getMyInfo(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(ApiResponse.ok(memberService.findByEmail(user.getUsername())));
  }
}
