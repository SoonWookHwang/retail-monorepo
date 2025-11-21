package com.retail.member.service;

import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import com.retail.common.security.JwtTokenProvider;
import com.retail.member.dto.LoginRequest;
import com.retail.member.dto.TokenResponse;
import com.retail.member.entity.Member;
import com.retail.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        request.getEmail(), request.getPassword()
    );

    String token = jwtTokenProvider.generateToken(authentication);
    return new TokenResponse(token);
  }
}
