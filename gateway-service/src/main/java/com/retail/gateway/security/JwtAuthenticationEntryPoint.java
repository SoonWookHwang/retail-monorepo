package com.retail.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.common.dto.ApiResponse;
import com.retail.common.dto.ErrorResponse;
import com.retail.common.exception.ErrorCode;
import com.retail.gateway.exception.GatewayErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {

    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ErrorResponse errorResponse = ErrorResponse.from(
        ErrorCode.UNAUTHORIZED,
        GatewayErrorCode.AUTH_FAILED.getCode()
    );

    ApiResponse<ErrorResponse> apiResponse = new ApiResponse<>(false, errorResponse);

    byte[] bytes;
    try {
      bytes = objectMapper.writeValueAsBytes(apiResponse);
    } catch (Exception ex) {
      bytes = "{\"success\":false,\"data\":{\"message\":\"Unauthorized\"}}".getBytes();
    }

    DataBuffer buffer = exchange.getResponse()
        .bufferFactory()
        .wrap(bytes);

    return exchange.getResponse().writeWith(Mono.just(buffer));
  }
}
