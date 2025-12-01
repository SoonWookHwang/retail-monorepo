package com.retail.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    String token = extractToken(exchange);

    if (token == null || !jwtTokenProvider.validateToken(token)) {
      return Mono.empty();
    }

    Claims claims = jwtTokenProvider.getClaims(token);

    String email = claims.getSubject();
    String role = claims.get("role", String.class);

    UserDetails principal = User.withUsername(email)
        .password("")
        .roles(role.replace("ROLE_", ""))
        .build();

    return Mono.just(
        new UsernamePasswordAuthenticationToken(
            principal,
            token,
            principal.getAuthorities()
        )
    );
  }

  private String extractToken(ServerWebExchange exchange) {
    String bearer = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}

