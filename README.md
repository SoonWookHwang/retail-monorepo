# 🏬 Retail Monorepo

**Spring Boot 3.5.7 / Java 17 기반 리테일 서비스 모노레포 프로젝트**

---

## 📦 Modules
| Module                     | Description                   |
|:---------------------------|:------------------------------|
| **common**                 | 공통 DTO / 예외처리 / Config 모듈     |
| **product-service**        | 상품 관리 API                     |
| **order-service**          | 주문 처리 API                     |
| **payment-service**        | 결제 처리 API                     |
| **inventory-service**      | 재고 관리 API                     |
| **recommendation-service** | 추천 시스템 API (Elasticsearch 연동) |

---

## ⚙️ Tech Stack
- **Spring Boot** 3.5.7
- **Gradle (Groovy DSL)**
- **Java 17**
- **JPA / H2 (Test) / MySQL (Main)**
- **Lombok / Validation / Spring Web**
- **Kafka / Redis / Elasticsearch** *(향후 확장 예정)*

---

## 🚀 Build & Run
```bash
./gradlew build
./gradlew :product-service:bootRun
```

## 🧩 Product Service Overview
 ### 📖 Description

product-service는 상품 관리 도메인을 담당하며,
상품(Entity)과 브랜드, 카테고리, 이미지, 재고 등의 연관관계를 중심으로 CRUD 기능을 제공합니다.

### 🧱 Entity Structure
| Entity           | Relation    | Description    |
|:-----------------|:------------|:---------------|
| **Product**      | -           | 상품의 중심 엔티티     |
| **Brand**        | @ManyToOne  | 브랜드 정보         |
| **Category**     | @ManyToOne  | 카테고리 정보        |
| **ProductImage** | @OneToMany  | 상품 이미지 (다수)    |
| **ProductStock** | @OneToOne   | 재고 수량 정보       |
| **Review**       | @OneToMany  | 리뷰 (추후 구현 예정)  |

> `@Builder.Default`를 사용해 컬렉션 초기화 시 NPE를 방지하며,  
> `Fetch Join` 쿼리(`findAllWithBrandAndCategory`)를 통해 N+1 문제를 예방했습니다.

---

## 🧠 주요 기능

- **상품 등록(Create)** : 브랜드, 카테고리, 이미지, 재고와 함께 Cascade 저장
- **상품 조회(Read)** : Fetch Join 기반으로 브랜드/카테고리 정보 즉시 로딩
- **상품 삭제(Delete)** : Cascade + OrphanRemoval로 관련 엔티티 자동 삭제
- **DTO 변환 구조** : `ProductRequest`, `ProductResponse` 로 명확한 입출력 분리

---
