## [Step14] Asynchronous Design Report
- **선착순 쿠폰발급 기능에 대해 Redis 기반의 설계**  
- **적절하게 동작할 수 있도록 쿠폰 발급 로직을 개선해 제출**
- **시스템 ( 랭킹, 비동기 ) 디자인 설계 및 개발 후 회고 내용을 담은 보고서 제출**
---

### 1. 선착순 쿠폰 발급 기능 개요
- 쿠폰을 일정 수량만큼, 먼저 요청한 순서대로 **유저 한 명당 하나의 쿠폰**을 발급

---

### 2. 선착순 쿠폰 발급 구현 방식
0. **Redis에 쿠폰 수량 관리**
   - Key 형태: `coupon:issue:!couponId!:quantity`
   - 쿠폰의 남은 수량을 저장
1. **중복 발급 체크**
   - BitMap 자료구조 사용
   - Key: `coupon:issue:!userId!:coupon:!couponId!:issued`
   - 이미 발급된 경우 중복 방지
2. **쿠폰 수량 감소**
   - `coupon:issue:!couponId!:quantity` Key의 값을 `decrement()`로 **원자적 감소**
3. **비동기 처리를 위한 Queue 등록**
   - Sorted Set 자료구조 사용: `coupon:issue:queue`
   - Value: `userId:couponId` (예: `"1:1"`)
   - Score: 현재 시각(밀리초)
4. **DB 등록 처리**
   - `@Scheduled`를 통해 매 초마다 `coupon:issue:queue`에서 score 기준으로 **50개씩 `popMin()`**
   - 조회한 값으로 `userCoupon`을 DB에 등록

---

### 3. 구현 방식 결정 배경
- 쿠폰 메타 정보는 DB에서 관리하지만 **남은 수량은 Redis에서 관리**
  - DB와 Redis를 분리하여 **정합성 유지 + 빠른 조회 가능**
- **중복 발급 여부**를 BitMap으로 관리하여 **메모리 효율 최적화**
- Redis의 원자성을 보장하기 위해 `decrement()` 사용
- `@Scheduled` 처리 시 한 번에 너무 적게 처리하면 UX 저하, 너무 많이 처리하면 DB 부담
  - 적정값 50개 설정
  - `redisTemplate.popMin()` 사용으로 **원자적 처리 보장**

---

### 4. 코드 및 테스트
- [실시간쿠폰발급 구현 및 테스트 코드 작성](d254fd7)
- [주석실수](d919b8b)
