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

import java.lang.reflect.Method;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    ServerHttpResponse response = exchange.getResponse();

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

  /**
   * ErrorCode 자동 변환 시스템
   * - CustomException 처리
   * - 도메인 Exception(MemberException 등) 자동 매핑
   * - ResponseStatusException 처리
   */
  private ErrorCode resolveErrorCode(Throwable ex) {

    // 1) 공통 CustomException 처리
    if (ex instanceof CustomException customEx) {
      return customEx.getErrorCode();
    }

    try {
      Method method = ex.getClass().getMethod("getErrorCode");
      Object domainErrorCode = method.invoke(ex);

      Method baseMethod = domainErrorCode.getClass().getMethod("getBase");
      return (ErrorCode) baseMethod.invoke(domainErrorCode);

    } catch (Exception ignore) {
      // 리플렉션 실패하면 다른 타입의 예외 → 기본 처리로 이동
    }

    if (ex instanceof ResponseStatusException statusEx) {
      int status = statusEx.getStatusCode().value();
      return switch (status) {
        case 400 -> ErrorCode.INVALID_REQUEST;
        case 404 -> ErrorCode.NOT_FOUND;
        default -> ErrorCode.INTERNAL_ERROR;
      };
    }
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
