package com.retail.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String token = authentication.getCredentials().toString();

    // 1) JWT 유효성 검사
    if (!jwtTokenProvider.validateToken(token)) {
      return Mono.empty();
    }

    // 2) Claims 추출
    Claims claims = jwtTokenProvider.getClaims(token);

    String email = claims.getSubject();
    String role = claims.get("role", String.class); // 예: ROLE_USER, ROLE_ADMIN

    // 3) Spring Security UserDetails 생성
    UserDetails principal = User.withUsername(email)
        .password("")  // JWT 인증에서는 사용하지 않음
        .roles(role.replace("ROLE_", "")) // Spring Security 규칙에 맞게 변환
        .build();

    // 4) Authentication 반환
    return Mono.just(
        new UsernamePasswordAuthenticationToken(
            principal,
            token,
            principal.getAuthorities()
        )
    );
  }
}
