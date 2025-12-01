package com.retail.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.common.dto.ApiResponse;
import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import com.retail.gateway.util.ErrorCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    ServerHttpResponse response = exchange.getResponse();
    // 이미 응답이 커밋된 경우 처리 불가
    if (response.isCommitted()) {
      return Mono.error(ex);
    }
    logError(ex);

    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ErrorCode errorCode = resolveErrorCode(ex);

    response.setStatusCode(ErrorCodeUtils.toHttpStatus(errorCode));

    ApiResponse<?> errorBody = ApiResponse.fail(errorCode);

    byte[] jsonBytes = serialize(errorBody);

    DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);
    return response.writeWith(Mono.just(buffer));
  }

  private ErrorCode resolveErrorCode(Throwable ex) {
    if (ex instanceof CustomException customEx) {
      return customEx.getErrorCode();
    }

    if (ex instanceof ResponseStatusException statusEx) {
      int status = statusEx.getStatusCode().value();
      return switch (status) {
        case 400 -> ErrorCode.INVALID_REQUEST;
        case 404 -> ErrorCode.NOT_FOUND;
        default -> ErrorCode.INTERNAL_ERROR;
      };
    }

    // 3) 그 외 예외는 Internal Server Error
    return ErrorCode.INTERNAL_ERROR;
  }
  private byte[] serialize(ApiResponse<?> response) {
    try {
      return objectMapper.writeValueAsBytes(response);
    } catch (Exception e) {
      log.error("Failed to serialize error response", e);
      return "{\"success\":false,\"data\":{\"message\":\"Internal error\"}}".getBytes();
    }
  }
  private void logError(Throwable ex) {
    log.error("[Gateway Error] {}", ex.getMessage(), ex);
  }
}
