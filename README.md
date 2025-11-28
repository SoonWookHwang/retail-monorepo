# 🏬 Retail Monorepo

**Spring Boot 3.5.7 / Java 17 기반 리테일 서비스 모노레포 프로젝트**
> Gateway-Service 중심의 MSA 구조 및 Docker 기반 통합 실행 환경 구축 (2025.11)

---

## 📦 Modules
| Module                     | Description                       |
|:---------------------------|:----------------------------------|
| **common**                 | 공통 DTO / 공통 Error code            |
| **gateway-service**        | API Gateway (WebFlux / JWT 인증 관리) |
| **member-service**         | 회원 관리 및 인증 처리 서비스                 |
| **product-service**        | 상품 관리 API                         |
| **order-service**          | 주문 처리 API                         |
| **payment-service**        | 결제 처리 API                         |
| **inventory-service**      | 재고 관리 API                         |
| **recommendation-service** | 추천 시스템 API (Elasticsearch 연동)     |

---

## ⚙️ Tech Stack
- **Spring Boot** 3.5.7 (WebFlux Gateway 포함)
- **Gradle (Groovy DSL)**
- **Java 17**
- **Spring Security / JWT (io.jsonwebtoken)**
- **JPA / MySQL (Docker) / H2 (Test)**
- **Lombok / Validation / Spring Web / WebFlux**
- **Docker Compose / Multi-Database 구성**
- **Kafka / Redis / Elasticsearch** *(확장 예정)*

---

## 🧩 Architecture Overview

### 🔐 Gateway-Service
- `spring-cloud-gateway` 기반 WebFlux 라우팅
- JWT 인증 및 인가 처리 담당 (Reactive Security)
- Member-Service로 인증 요청 전달 및 필터링 수행

### 👥 Member-Service
- 사용자 등록, 로그인, JWT 발급 담당
- BCryptPasswordEncoder 기반 패스워드 암호화
- Gateway 통과 후 서비스 접근 가능

### 🧱 Product-Service
- 상품, 브랜드, 카테고리, 이미지, 재고 CRUD 담당
- Cascade + OrphanRemoval 설정으로 연관 데이터 자동 관리

---

## 🐳 Docker Environment Setup

### 📁 주요 구성
```
retail-monorepo/
 ├── docker/
 │    ├── init.sql              # 초기 DB 생성 스크립트 (multi-database)
 │    ├── data/                 # MySQL 볼륨 데이터
 ├── docker-compose.yml          # 전체 서비스 구성 파일
 ├── gateway-service/
 ├── member-service/
 └── product-service/
```

---

## 🚀 실행 방법

### ✅ 1. 로컬 개발 환경 (bootRun)
```bash
# 모듈별 개별 실행
./gradlew :member-service:bootRun
./gradlew :product-service:bootRun

# application-local.yml 사용
# DB는 localhost:3307 연결 필요 (MySQL 실행 필수)
```

---

### 🐋 2. Docker 환경 실행
```bash
# 전체 서비스 빌드 및 실행
./gradlew clean build --refresh-dependencies
docker compose down -v
docker compose up -d --build

# 로그 확인
docker logs retail-gateway -f
docker logs retail-member -f
docker logs retail-product -f
```


## 🗂 Database Initialization (`docker/init.sql`)
```sql
CREATE DATABASE IF NOT EXISTS retail_member CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS retail_product CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS retail_order CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS retail_inventory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS retail_payment CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## ⚙️ Spring Profiles
| Profile | Purpose | DB Host |
|:---------|:----------|:----------|
| **local** | 로컬 개발용 (`bootRun`) | `localhost:3307` |
| **docker** | 컨테이너 실행용 (`ENV SPRING_PROFILES_ACTIVE=docker`) | `mysql:3306` |

> 각 서비스별로 `application-local.yml` / `application-docker.yml`을 분리하여 유지관리 용이성 확보.

---

## 📜 Recent Updates (2025.11.28)
- ✅ Gateway-Service 신규 구현 (WebFlux + JWT)
- ✅ Common 모듈에서 Security 완전 분리
- ✅ Docker Compose 기반 멀티 DB 구성
- ✅ 각 서비스별 독립 DB 관리 및 `ddl-auto=update`
- ✅ init.sql 자동 실행 및 Health Check 추가
- ✅ 로컬/도커 환경 분리 프로필 구성 완료

---

## 📖 실행 요약

| 단계 | 명령어 | 설명 |
|------|--------|------|
| 🧹 Clean Build | `./gradlew clean build` | 모든 모듈 빌드 |
| 🐳 Docker 실행 | `docker compose up -d --build` | 전체 서비스 컨테이너 실행 |
| 🧭 Gateway 확인 | `curl http://localhost:8080` | Gateway 진입점 확인 |
| 🧩 DB 확인 | `docker exec -it retail-mono-mysql mysql -uroot -p1234` | DB 연결 확인 |

---

## 🧾 프로젝트 목표
> "Think in Code. Deliver in Value."  
> 리테일 MSA 백엔드 시스템

---

**Author:** 황순욱  
**Last Updated:** 2025-11-28  
📧 bravadosw@naver.com  
🔗 GitHub: [github.com/SoonWookHwang](https://github.com/SoonWookHwang)
