# 🏬 Retail Monorepo

**Spring Boot 3.5.7 / Java 17 기반 리테일 서비스 모노레포 프로젝트**

## 📦 Modules
| Module                        | Description |
|:------------------------------|:----------------------------------------------------|
| **common**                    | 공통 DTO / 예외처리 / Config 모듈 |
| **product-service**           | 상품 관리 API |
| **order-service**             | 주문 처리 API |
| **payment-service**           | 결제 처리 API |
| **inventory-service**         | 재고 관리 API |
| **recommendation-service**    | 추천 시스템 API (Elasticsearch 연동) |

## ⚙️ Tech Stack
- Spring Boot 3.5.7
- Gradle (Groovy DSL)
- Java 17
- JPA, MySQL, Lombok
- Kafka, Redis, Elasticsearch (향후 추가)

## 🚀 Build & Run
```bash
./gradlew build
./gradlew :product-service:bootRun
