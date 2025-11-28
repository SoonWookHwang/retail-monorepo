package com.retail.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.common.dto.ApiResponse;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ApiResponse<String> apiResponse = ApiResponse.fail("Unexpected error occurred");
    byte[] bytes = new byte[0];
    try {
      bytes = new ObjectMapper().writeValueAsBytes(apiResponse);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    DataBuffer buffer = response.bufferFactory().wrap(bytes);
    return response.writeWith(Mono.just(buffer));
  }
}
