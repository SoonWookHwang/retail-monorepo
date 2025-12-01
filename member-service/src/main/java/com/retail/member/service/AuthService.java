package com.retail.member.service;

import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import com.retail.member.dto.LoginRequest;
import com.retail.member.dto.TokenResponse;
import com.retail.member.entity.Member;
import com.retail.member.exception.MemberErrorCode;
import com.retail.member.exception.MemberException;
import com.retail.member.repository.MemberRepository;
import com.retail.member.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public TokenResponse login(LoginRequest request) {

    Member member = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new MemberException(MemberErrorCode.LOGIN_FAILED));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new MemberException(MemberErrorCode.LOGIN_FAILED);
    }

    String token = jwtTokenProvider.generateToken(
        member.getId(),
        member.getEmail(),
        member.getRole().name()
    );

    return new TokenResponse(token);
  }
}
