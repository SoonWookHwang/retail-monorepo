package com.retail.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtUserIdInjectorFilter implements GlobalFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = extractToken(exchange);
    if (token != null && jwtTokenProvider.validateToken(token)) {
      Claims claims = jwtTokenProvider.getClaims(token);
      Long userId = claims.get("userId", Long.class);
      exchange = exchange.mutate()
          .request(builder -> builder.header("X-User-Id", String.valueOf(userId)))
          .build();
    }

    return chain.filter(exchange);
  }

  private String extractToken(ServerWebExchange exchange) {
    String bearer = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
