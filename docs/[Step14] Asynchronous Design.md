## [Step14] Asynchronous Design Report
- **선착순 쿠폰발급 기능에 대해 Redis 기반의 설계**  
- **적절하게 동작할 수 있도록 쿠폰 발급 로직을 개선해 제출**
- **시스템 ( 랭킹, 비동기 ) 디자인 설계 및 개발 후 회고 내용을 담은 보고서 제출**
---

### 1. 선착순 쿠폰 발급 기능 개요
- 쿠폰을 일정 수량만큼, 먼저 요청한 순서대로 유저 한명 당 하나의 쿠폰을 발급합니다.

### 2. 선착순 쿠폰 발급 구현 방식
0. Redis에 `coupon:issue:!couponId!:quantity`라는 Key형태로 쿠폰의 남은 수량이 보관되어있다고 가정합니다.
1. 실시간 쿠폰 발급 요청 시 BitMap 자료구조의 `coupon:issue:!userId!:coupon:!couponId!:issued` Key를 통해 중복 발급을 체크합니다
2. 중복되지 않았다면 `coupon:issue:!couponId!:quantity` Key을 통해 쿠폰의 수량을 `decrement()`을 통해 원자적으로 값을 감소합니다
3. 이후 비동기 처리를 위해 Queue역할을 하는 Sorted Set 자료구조의 `coupon:issue:queue` Key에 value는 아래와 같이 그리고 score는 현재 시각의 밀리초를 주입합니다.
```
value = userId + ":" + couponId
ex) "1:1"
```
4. `@Scheduled`를 통해 매 초 마다 `coupon:issue:queue`에 담긴 value 값을 score를 기준으로 `pop`형태로 50개를 조회하여 `userCoupon`을 DB에 등록합니다.

### 3. 선착순 쿠폰 발급 구현 방식 결정 배경
- 쿠폰의 발급 수량을 포함한 메타 정보는 DB에서 관리하지만 쿠폰의 남은 수량은 쿠폰 ID와 함께 레디스로 관리하였습니다
  - 이와 같은 구현 방식으로 인해 쿠폰 메타 정보의 정합성을 지키며 캐시을 통한 빠른 조회도 가능하였습니다.
- 유저의 쿠폰 발급 여부를 타 자료구조가 아닌 BitMap 자료구조를 사용하여 메모리 사용을 최소화하였습니다.
- 레디스의 원자성을 보장하기 위해 수량 감소 로직에서 `get and set` 방식이 아닌 `redisTemplate.decrement()`을 사용하였습니다.
- `@Scheduled`을 매 초마다 실행을 시키더라도 10개씩 조회를 하면 UX 측면에서 좋지 못할 것으로 판단되고 100개는 DB에 부담이 될수 있다고 생각이 되어 50개로 설정 및 위와 같은 이유로 `redisTeamplte.popMin()`을 통해 원자성을 보장하였습니다.


### 4. 선착순 쿠폰 발급 코드
- [실시간쿠폰발급 구현 및 테스트 코드 작성](d254fd7)
- [주석실수](d919b8b)
