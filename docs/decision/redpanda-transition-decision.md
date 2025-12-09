# 🐼 Redpanda 전환 결정 문서
**작성일:** 2025-12-08  
**작성자:** 황순욱  
**위치:** `/docs/decision/redpanda-transition-decision.md`

---

# 🎯 1. 왜 Kafka → Redpanda 전환을 결정했는가?

프로젝트의 로컬 MSA 개발 환경에서 Kafka(Zookeeper 포함)를 사용했을 때,  
Docker Desktop 메모리 부족(8GB)으로 인해 다음 문제가 지속적으로 발생함:

- Kafka 컨테이너 **OOM(OutOfMemoryError)** 발생
- Zookeeper Healthcheck 실패 → Kafka 구동 불가
- 전체 `docker compose` 메모리 점유율 7~8GB 도달
- IDE / Docker Desktop / 브라우저까지 시스템 전체가 버벅임
- 개발 단계에서 Kafka를 실행하기가 어려워 생산성 저하

이를 해결하기 위해 **경량 이벤트 스트리밍 플랫폼인 Redpanda**로 전환함.

---

# 🚀 2. Redpanda 선택 이유

## ✅ 2.1 Zookeeper가 필요 없음 (ZK-less 구조)

Kafka는 운영 시 **Zookeeper가 필수**이지만,  
Redpanda는 내부적으로 자체 Consensus Layer를 사용하여 **Zookeeper 없이 동작**함.

👉 **컨테이너 1개만 구동하면 Kafka API 완전 호환**

→ 로컬 개발환경에서 리소스 절감 효과가 매우 큼  
→ 개발자가 구동해야 하는 시스템 부하가 절반 이하로 감소

---

## ✅ 2.2 Kafka-Compatible → 기존 코드 수정 필요 거의 없음

Redpanda는 다음을 100% 지원함:

- Kafka API (Produce / Consume / Admin)
- Kafka Serde(JsonSerializer/Deserializer)
- Kafka Consumer Group
- Kafka Topic 메타데이터 구조
- Spring Kafka, Kafka Client

즉,

> **spring.kafka.bootstrap-servers: kafka:9092**  
그대로 사용해도 정상 동작함.

따라서 Order-Service, Payment-Service의 **기존 Kafka Producer/Listener 코드를 전혀 수정할 필요 없음.**

---

## ✅ 2.3 로컬 개발환경에서 Kafka 대비 메모리 80% 절감

### 기존 구성 (Kafka + Zookeeper)

| 서비스 | 메모리 |
|--------|---------|
| Kafka | 512MB~1GB |
| Zookeeper | 512MB |
| 합계 | 약 1GB~1.5GB |

### Redpanda 구성

| 서비스 | 메모리 |
|--------|---------|
| Redpanda 1개 | **256MB** |

👉 **Kafka 대비 메모리 70~80% 절약**  
👉 Docker Desktop 4GB 환경에서도 안정적으로 구동 가능  

---

## ✅ 2.4 개발자 경험(Developer Experience)이 훨씬 뛰어남

Redpanda는 다음과 같은 장점을 제공:

- `rpk` CLI로 topic 생성/조회가 매우 쉬움  
- Kafka보다 훨씬 빠른 startup time  
- 메모리/CPU 사용량 최적화  
- 로컬 개발에 최적화된 실행 플래그 제공 (`--overprovisioned` 등)

로컬에서 메시지 기반 구조(MSA Event-driven)를 개발할 때 번거로움이 크게 감소함.

---

# 🧩 3. 운영환경에서는 왜 Kafka를 사용해야 하는가?

Redpanda는 로컬 개발환경에서 매우 유리하지만,  
운영 환경에서는 Kafka를 그대로 유지하는 것이 바람직함.

## 🔒 3.1 운영 환경 안정성 및 검증된 신뢰성

Kafka는 수년간 대규모 트래픽 환경에서 검증된 표준이며,  
대기업, 금융권, 커머스, 물류 등에서 널리 사용됨.

➡ 운영 환경 장애 대응 문서가 많고, 운영 인력이 Kafka에 익숙함.

---

## 🏭 3.2 대규모 클러스터 운영 시 성숙도 차이

Kafka는 수백 노드를 운영하는 대규모 환경에 최적화되어 있으며,  
Redpanda는 새로운 기술이기 때문에

- 운영 경험 부족  
- 장애 사례 데이터 적음  
- 엔터프라이즈 기능이 유료  

같은 한계가 있음.

---

## ☁ 3.3 클라우드 인프라(Kafka on MSK 등)와의 통합

AWS, GCP, Azure 모두 Kafka 관리형 서비스를 제공하지만  
Redpanda는 일부 클라우드에서 공식 지원이 제한됨.

따라서 운영은 Kafka, 로컬 개발은 Redpanda 조합이 가장 합리적임.

---

# 📌 결론

### ✔ 로컬 개발 환경 — **Redpanda 사용**  
- Kafka 100% 호환  
- 메모리 절감  
- 빠르고 안정적  
- 컨테이너 수 감소  
- 개발 편의성↑

### ✔ 운영 환경 — **Kafka 사용**  
- 검증된 안정성  
- 대규모 트래픽 대응  
- 인프라 표준화  

👉 **"개발 환경 최적화 + 운영 환경 안정성"을 모두 확보하는 전략"**

---