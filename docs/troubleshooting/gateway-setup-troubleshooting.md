# 🧩 Gateway-Service 및 Docker 환경 트러블슈팅 기록  
**작성일:** 2025-11-28  
**작성자:** 황순욱  
**브랜치:** `feature/gateway-service`

---

## 🚀 1. Gateway-Service 구조 및 보안 설정 분리

### 🔧 작업 내용
- 기존 `common` 모듈에 존재하던 보안 관련 클래스 제거:
  - `JwtAuthenticationFilter`
  - `JwtTokenProvider`
  - `SecurityConfigBase`
  - `AuthenticatedUser`
- `gateway-service` 내 WebFlux 기반 보안 구조 신규 작성:
  - `GatewaySecurityConfig`
  - `JwtAuthenticationManager`
  - `JwtServerAuthenticationConverter`
  - `JwtAuthenticationWebFilter`
- 각 서비스(`member-service`, `product-service` 등)는 Gateway를 통해 인증된 요청만 접근 가능하도록 설정 단순화

### ⚠️ 트러블슈팅
- **문제:** `conversionServicePostProcessor` 중복 에러 발생  
- **원인:** `spring-boot-starter-web` + `spring-boot-starter-security` 혼용으로 Servlet/Reactive 충돌  
- **해결:** Gateway는 WebFlux만 유지하고, Common 모듈에서 Security 의존성 완전 제거  

---

## 🧱 2. Common 모듈 경량화

### 🔧 작업 내용
- 공통 모듈에서 보안 및 설정 관련 클래스 완전 제거
- `GlobalExceptionHandler` 삭제, 단 각 서비스별로 독립 ExceptionHandler 작성
- `ErrorCode` Enum을 단순화하고 `HttpStatus` 의존 최소화
- `ApiResponse.fail()` 포맷 유지로 일관된 응답 구조 확보

### ⚠️ 트러블슈팅
- **문제:** Gateway 실행 시 Common 모듈의 Security 빈 충돌  
- **해결:** Common의 build.gradle에서 `spring-boot-starter-security` 제거  

---

## 🧭 3. Docker 및 DB 초기화 구성

### 🔧 작업 내용
- 서비스별 DB 분리 (init.sql)
  ```sql
  CREATE DATABASE IF NOT EXISTS retail_member CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  CREATE DATABASE IF NOT EXISTS retail_product CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  CREATE DATABASE IF NOT EXISTS retail_order CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  CREATE DATABASE IF NOT EXISTS retail_inventory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  CREATE DATABASE IF NOT EXISTS retail_payment CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
- MySQL 볼륨 유지 경로:  
  `/var/lib/mysql → ./docker/data`
- 초기 SQL 스크립트:  
  `/docker-entrypoint-initdb.d/init.sql → ./docker/init.sql`
- 서비스별 `Dockerfile` 추가 및 포트 지정:
  - member-service → 8081
  - product-service → 8082
  - gateway-service → 8080

### ⚠️ 트러블슈팅
| 문제 | 원인 | 해결 |
|------|------|------|
| `Unknown database 'retail_product'` | MySQL 볼륨이 기존 데이터를 유지하여 init.sql 미실행 | `docker compose down -v` 실행 후 재시작 |
| `Public Key Retrieval is not allowed` | MySQL Connector의 인증 정책 | JDBC URL에 `allowPublicKeyRetrieval=true` 추가 |

---

## 🌐 4. Gateway → Member-Service 라우팅 문제

### 🔧 작업 내용
- Postman에서 `http://localhost:8080/api/members/signup` 테스트
- Gateway `application.yml` 라우팅 설정 추가:
  ```yaml
  spring:
    cloud:
      gateway:
        routes:
          - id: member-service
            uri: http://member-service:8081
            predicates:
              - Path=/api/members/**
  ```
- 도커 네트워크 환경에서 서비스명(`member-service`)을 Host로 사용하도록 수정

### ⚠️ 트러블슈팅
- **문제:** Gateway는 500 오류, 실제 원인은 Member-Service 내부 예외  
- **해결:** `docker logs retail-member` 통해 내부 로그 확인 → DB 연결 예외 처리  

---

## 🧰 5. 로컬은 정상, Docker에서는 오류나는 현상

### 🔍 원인 분석
- `bootRun`에서는 `localhost:3307` 연결 정상  
- Docker 환경에서는 `mysql:3306` 연결 실패  
- 내부 네트워크 명 불일치로 인한 연결 오류

### ✅ 해결
- Compose 내 `mysql` 서비스명과 JDBC 호스트명을 일치시킴  
- 모든 서비스의 `SPRING_DATASOURCE_URL`을 다음 포맷으로 통일:
  ```yaml
  jdbc:mysql://mysql:3306/{DB명}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
  ```

---

## 🧩 6. Git 관리 및 커밋 전략

### 🔧 작업 내용
- Gateway 관련 보안 및 필터 추가 작업을 단독 커밋:
  ```
  feat(gateway-service): implement JWT authentication and global error handler
  ```
- 이후 전체 Monorepo 수정(공통 설정, Docker 등)을 별도 커밋:
  ```
  chore(monorepo): update shared configs, docker setup, and service adjustments
  ```

---

## ✅ 현재 서비스 구조 (2025-11-28 기준)

```
retail-monorepo/
 ├── common/
 │    ├── exception/
 │    └── dto/
 ├── gateway-service/
 │    ├── config/
 │    ├── security/
 │    └── Dockerfile
 ├── member-service/
 │    ├── security/
 │    ├── exception/
 │    └── Dockerfile
 ├── product-service/
 │    ├── exception/
 │    └── Dockerfile
 ├── docker/
 │    ├── init.sql
 │    └── data/
 └── docker-compose.yml
```

---

## 🧾 요약

| 구분 | 주요 변경 사항 | 상태 |
|------|----------------|------|
| 공통 모듈 | 보안 및 예외 제거, 경량화 | ✅ 완료 |
| Gateway | JWT 인증 및 라우팅 처리 | ✅ 완료 |
| 각 서비스 | 개별 DB 및 예외 핸들러 구성 | ✅ 완료 |
| Docker | 다중 DB + 헬스체크 + 볼륨 구성 | ✅ 완료 |
| Git | gateway-service / monorepo 별 커밋 분리 | ✅ 완료 |

---

> **결론:**  
> Gateway를 통한 인증/인가 구조로 MSA 보안 체계를 정비했고,  
> Docker 기반으로 각 서비스가 독립적으로 DB를 관리할 수 있게 개선 완료.  
> 공통 보안 충돌, DB 연결 이슈, 초기 SQL 미적용 문제 모두 해결됨 ✅
