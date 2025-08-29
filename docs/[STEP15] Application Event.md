## [Step15] Application Event
- **주문/예약 정보를 원 트랜잭션 종료 이후 전송**
- **부가 로직(주문/예약 정보 전달)을 메인 서비스 로직과 분리**

---

### 1. Application Event 개요
- 비동기로 처리 가능한 부가 로직은 `Application Event` 기반의 비동기 이벤트로 전환하여 구현할 수 있습니다.

---

### 2. Application Event 구현 방식

#### 1) 이벤트 정의
```java
public record PaymentCompletedEvent(
    long orderItemId,
    long orderItemQuantity,
    long orderItemPrice,
    long orderItemTotalPrice,
    long orderId,
    long productId
) {}
```
Application Event 객체는 불변성을 유지하는 것이 일반적이므로 `record`를 사용해 정의하였습니다.

#### 2) 이벤트 발행
```java
private final ApplicationEventPublisher applicationEventPublisher;

applicationEventPublisher.publishEvent(new PaymentCompletedEvent(
    orderItem.getId(),
    orderItem.getQuantity(),
    orderItem.getPrice(),
    orderItem.getTotalPrice(),
    orderItem.getOrderId(),
    orderItem.getProductId()
));
```
`ApplicationEventPublisher`를 주입받아 `publishEvent()` 메서드로 이벤트 객체를 발행합니다.

#### 3) 이벤트 리스너
```java
@Async
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void sendOrderData(PaymentCompletedEvent event) {
    System.out.println("event : " + event);
}
```
발행된 이벤트(`PaymentCompletedEvent`)는 이벤트 리스너에서 처리됩니다.  
리스너 메서드의 **인자 타입**이 이벤트와 매핑 기준이 됩니다.

---

### 3. 이벤트 적용 사례
다음 기능들을 비동기 이벤트로 분리하여 처리했습니다.
- 결제 성공 시 주문 상세 정보를 외부 데이터 플랫폼 API로 전송
- 결제 성공 시 Redis에 상품별 판매 수량 기록
- 포인트 사용/충전 시 DB에 포인트 내역 등록
  <br> [추후 Kafka 도입 시 등록 실패에 대비한 **retry 전략** 적용 예정]
- 실시간 쿠폰 발급 시 Redis Queue에 Request 정보 기록

---

### 4. 도입 배경
- 사용자에게 **더 빠른 응답** 제공
- 메인 로직과 부가 로직을 분리하여 **처리 속도 및 유지보수성 향상**
