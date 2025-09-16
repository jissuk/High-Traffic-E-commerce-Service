## [Step13] Ranking Design
- **가장 많이 주문한 상품 랭킹을 Redis 기반으로 설계**  
- **설계를 기반으로 개발 및 구현**

---

### 1. 인기 판매 상품 조회 기능 개요
- 최근 3일간 판매 수량이 많은 3개의 상품을 조회

---

### 2. 인기 판매 상품 조회 구현 방식
1. **결제 시 Redis Sorted Set에 데이터 주입**
   - Key: `product:sales:YYYY-MM-DD`
   - Value: 상품 ID
   - Score: 주문 수량
   - 동일 상품 ID는 결제 시마다 score 증가

   ```text
   Key: product:sales:2025-08-22
   Value: 상품ID
   Score: 주문 수량
   ```

2. **3일 데이터 집계**
   - 매일 00:05, 최근 3일 데이터를 집계
   - 집계 결과를 `product:sales:3days:total` 키로 생성/갱신
   - Score 역순으로 인기 상품 조회 가능

3. **조회 시 처리**
   - `product:sales:3days:total`에서 상품 ID 조회 후 DB에서 상세 정보 반환

---

### 3. 구현 방식 결정 배경
- Redis에서 직접 조회 대신 **DB에서 조회**하도록 결정
  - DB와 Redis 간 상품 정합성 문제
  - Redis 의존성 증가 방지
  - 동일 데이터를 두 곳에서 관리 시 관리 포인트 증가
  - DB PK 조회 시 클러스터링 인덱스로 인해 성능 저하 미비
- 위와 비슷한 목적으로 `@Scheduled`로 4일전의 데이터를 정각에 삭제 후 5분이 지난 후에 3일간의 데이터 집계
   
---

### 4. 코드 및 테스트
- [인기상품조회 구현 및 테스트 코드 작성](https://github.com/jissuk/hhplus_eCommerce_java/pull/8/commits/425cf6ff013e1d507bcb4e67577aa009afb5d897)
- [인기상품조회 Scheduled 코드 및 Repository 코드 작성](https://github.com/jissuk/hhplus_eCommerce_java/pull/8/commits/7079b9bb2b1a3264e8da4bf2a8c1b9eeca551b3b)
