# 🛠️ Order-Service & Product-Service Integration Troubleshooting  
**작성일:** 2025-12-04  
**작성자:** 황순욱  
**브랜치:** `feature/product-internal-api-for-order`

---

## 🚀 1. Feign Client 제거 및 WebClient 전환

### 🔧 변경 배경
- Spring Boot 3.5.x는 Spring Cloud 2023.x와 **호환되지 않음**  
- Feign Client 사용 시 부트 실행 단계에서 다음 오류 발생:
  ```
  Spring Boot [3.5.7] is not compatible with this Spring Cloud release train
  ```
- Boot 버전을 3.3.x로 다운그레이드하는 방식은 프로젝트 전체 영향도가 너무 커서 제외  
- **WebClient는 Spring WebFlux 기본 모듈에 포함되므로** 버전 충돌 없이 사용 가능

### ✨ 해결
- order-service에서 Feign 관련 모든 의존성과 코드 제거
- WebClient 기반 ProductClient 구현
- 공통 설정용 `WebClientConfig` 추가

---

## 🔧 2. Order-Service → Product-Service 내부 연동 API 구축

### 🧩 작업 내용
- product-service에 order-service 전용 내부 API 추가:
  - `/internal/products/{id}` : 상품 상세 조회
  - `/internal/products/{id}/decrease-stock` : 재고 차감 API
- InternalProductService 도입
  - ProductStock 대상 비관적 락 적용한 안전한 재고 차감
  - ProductErrorCode + ProductException 기반 예외 구조 개선
- ProductStockRepository 추가하여 직접 재고 row lock 처리

### ⚠️ 문제 & 해결
| 문제 | 원인 | 해결 |
|------|------|------|
| IllegalArgumentException 반복 | 공통 예외 처리 부족 | ProductException 도입 |
| 동시 재고 차감 시 오류 | Product 엔티티 직접 접근 | Stock 엔티티 전용 Repository 사용 |
| order-service에서 ProductStock null 발생 | stock 초기화 누락 | Dummy 데이터 생성 시 stock 생성 로직 점검 |

---

## 🔥 3. Docker 환경에서 Product-Service Healthcheck 실패

### ❗ 증상
- order-service는 product-service healthy 상태를 기다리지만  
  product-service가 계속 **unhealthy → ERROR** 상태로 종료됨

### 🔍 원인
- Actuator 설정은 application.yml (root)에 존재  
- application-docker.yml에는 Actuator exposure 설정이 없음  
- Docker에서는 `SPRING_PROFILES_ACTIVE=docker`로 실행되므로  
  **health endpoint 설정이 적용되지 않아 404 응답 → unhealthy**

### ✅ 해결
`application-docker.yml`에 아래 설정 추가:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: always
```

---

## 🔧 4. WebClient Bean 미등록 오류

### 오류 메시지
```
Parameter 0 of constructor in ProductClient required a bean of type 
'org.springframework.web.reactive.function.client.WebClient' that could not be found.
```

### 원인
- WebClient는 WebFlux 패키지에 존재  
- 단독 사용 시 반드시 `WebClientConfig`에서 Bean 등록 필요

### 해결
```java
@Configuration
public class WebClientConfig {
  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }
}
```

---

## 📦 5. Docker Compose 서비스 의존성 및 DB 생성 문제

### 🧩 작업 내용
- init.sql에 retail_order DB가 누락 → 추가
- order-service에 product-service 의존성 추가:
  ```yaml
  depends_on:
    product-service:
      condition: service_healthy
  ```
- Product-Service healthcheck가 정상 전달되도록 actuator 설정 필수

### ⚠️ 문제 & 해결 정리
| 문제 | 원인 | 해결 |
|------|------|------|
| `Unknown database 'retail_order'` | init.sql 누락 | SQL 추가 |
| order-service가 product-service보다 빨리 실행 | depends_on 구성 | healthcheck 기반 종속 |
| product-service healthcheck 실패 | actuator 설정 누락 | docker 프로필에 health expose 추가 |

---

## ⚙️ 6. Product-Service 내부 코드 개선

### 수정 사항
- ProductErrorCode에 toException() 로직 추가
- ProductException 기반 일관된 예외 처리 도입
- ProductStockRepository 생성 후 `findForUpdate` 메서드 구현
- product-service build.gradle에서 Actuator 추가
- Dockerfile에 Web 프로필 적용을 위한 환경 변수 유지

---

## 🧾 요약

| 카테고리 | 해결 내역 | 상태 |
|---------|-----------|------|
| Spring Cloud 비호환 | Feign 제거, WebClient 전환 | ✅ 완료 |
| product-service 내부 API | 조회 + 재고 차감 API 구축 | ✅ 완료 |
| 재고 차감 동시성 | ProductStock 전용 Repo + 비관적 락 | ✅ 완료 |
| Docker healthcheck | actuator expose 수정 | ✅ 완료 |
| DB 초기화 | retail_order 추가 | ✅ 완료 |
| 주문 생성 흐름 | snapshot → 결제(추후) → 재고차감 | ✅ 완료 |

---

## 📌 결론  
이번 작업을 통해 **order-service ↔ product-service 간 내부 통신 구조를 안정적으로 구축**하였으며,  
Spring Boot 3.5.x 환경에서도 문제 없이 서비스 간 통신이 가능하도록 WebClient 기반 구조로 전환 완료했다.

Docker 환경에서 발생하던 healthcheck 및 DB 초기화 문제도 해결하여  
MSA 구성에서의 부트스트랩 안정성이 대폭 향상되었다.

---