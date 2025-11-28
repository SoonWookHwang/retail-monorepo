package com.retail.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    String bearer = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (bearer != null && bearer.startsWith("Bearer ")) {
      String token = bearer.substring(7);
      return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
    }
    return Mono.empty();
  }
}
