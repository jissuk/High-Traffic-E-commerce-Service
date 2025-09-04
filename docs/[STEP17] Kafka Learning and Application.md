## [Step17] 카프카 기초 학습 및 활용
- **카프카에 대한 기본 개념 학습 문서 작성**
- **실시간 주문 정보를 카프카 메시지로 발행**

---

## 카프카에 대한 기본 개념 학습 문서 작성

### 카프카 원리
카프카는 **메시지를 디스크(로그 파일)에 순차적으로 기록**하는 구조를 가지고 있습니다.

카프카가 디스크 기반으로 작동함에도 **메모리 기반 메시지 큐보다 빠르거나 비슷한 성능**을 내는 이유는 아래와 같습니다.
- **파일 시스템 캐시(Page Cache)**
- **Zero-copy**
- **순차 작성(Sequential Write)**

---

### 파일 시스템 캐시(Page Cache) 동작 방식
- 리눅스와 같은 OS 커널은 **최근에 읽거나 쓴 파일 데이터를 메모리에 캐싱**하는데 이 캐시를 **Page Cache**라고 부릅니다.
- 애플리케이션(카프카 포함)은 디스크에서 직접 데이터를 가져오는 게 아니라, 우선 OS 캐시에 있는지 확인 후 가져옵니다.

즉, 카프카가 디스크에서 로그를 읽는다고 해도, **실제로는 메모리(Page Cache)에서 읽는 경우가 대부분**이기에 빠른 속도를 제공하는 것입니다.

하지만 단점으로는 Page Cache 즉 OS 캐쉬는 전체 시스템의 메모리를 사용하기에 카프카에 부담이 많아질수록 시스템 전체에 영향이 끼칠 수 있습니다.

---

### Zero-copy
Zero-copy는 데이터를 전송할 때 CPU와 메모리 간에 불필요한 복사 과정을 제거하는 기술입니다.  
애플리케이션 버퍼로의 복사와 소켓 버퍼로의 복사를 생략하여 CPU 사용량을 줄이고 데이터 전송 속도를 크게 높입니다.

하지만 단점으로는 애플리케이션이 데이터에 직접 접근할 수 없다는 점이 있습니다.

---

### 카프카의 순차 작성(Sequential Write) 패턴
- 카프카는 메시지를 Topic의 **Partition 로그 파일 끝에 순차적으로 추가**합니다.
- "어느 Offset 이후부터 읽는다"라는 방식이므로 RDBMS의 DB처럼 랜덤 접근할 필요가 없습니다.
- 따라서 인덱스 관리 오버헤드도 거의 없고, 디스크 성능(특히 순차 쓰기)을 최대한 활용할 수 있습니다.

위와 같은 이유로 카프카는 순차 보장과 빠른 로그 처리가 가능합니다.

---

### 컨슈머 그룹
카프카의 컨슈머 그룹은 하나 이상의 Consumer를 묶어놓은 그룹입니다.  
메시지를 읽는 Consumer는 반드시 이 그룹에 속해야 합니다.

**주요 역할 및 기능**
- **오프셋 관리의 독립성**: 각 컨슈머 그룹은 자신이 소비하는 파티션별로 독립적인 오프셋(offset)을 관리하며 그룹마다 '어디까지 읽었는지'를 개별적으로 기록합니다.
- **데이터 다중 활용**: 하나의 데이터 스트림을 여러 용도로 사용할 수 있습니다.

예를 들어, 리뷰 시스템과 레거시 DB 싱크처럼 서로 다른 그룹들은 각자의 오프셋을 기반으로 서로 다른 시점부터 데이터를 읽어 들일 수 있습니다.

[참고자료]  
<br> ![image](https://github.com/user-attachments/assets/5590bc22-78b1-406d-a675-8242f8bca4c4)

---

## 실시간 주문 정보를 카프카 메시지로 발행

### 0. 카프카 설정
구현 코드 : [kafka설정](https://github.com/jissuk/hhplus_eCommerce_java/blob/step17/src/main/resources/application.yml)

초기에는 `application.yml`에 아래와 같은 설정을 하여 `KafkaTemplate<key,value>`의 value가 자동으로 직렬화되도록 설정하였습니다.  
하지만 보안상의 이슈로 추후 `ObjectMapper`를 사용하는 방식으로 변경하였습니다.

```yaml
spring:
  kafka:
    producer:
      properties:
        spring:
          json:
            trusted:
              packages: "*"
```

메시지 브로커에 등록할 데이터는 영구적으로 저장할 데이터가 아닌 임시 데이터이기에 **유효 기간을 1일로 설정**해두었습니다.

```yaml
spring:
  kafka:
    admin:
      properties:
        topic:
          configs:
            retention.ms: 86400000 # 메시지 보관 시간: 1일
            cleanup.policy: "delete"
```

---

### 1. 구현 방식
`RegisterPaymentUseCase` 일부 코드

```java
TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
    @Override
    public void afterCommit() {
        kafka.send(PAYMENT_COMPLETE_TOPIC, jsonOrderItem);
    }
});
```

- `TransactionSynchronizationManager.registerSynchronization()`를 통해 **트랜잭션 커밋이 완료된 이후** 로직이 실행되도록 작성하였습니다.

`OrderDataSenderListener` 코드 일부

```java
@KafkaListener(topics = PAYMENT_COMPLETE_TOPIC, groupId = SEND_ORDER_DATA_SERVICE)
public void sendOrderData(String orderItem) {
    System.out.println("orderItem : " + orderItem);
}
```

---

### 2. 구현 방식 결정 배경
- 기존 ApplicationEvent를 대체하는 방식으로 구현하였습니다.


---

### 3. 구현 코드 
- 구현 코드 : [feat: 실시간 주문 정보를 카프카 메시지로 발행](https://github.com/jissuk/hhplus_eCommerce_java/commit/9f9829db227537de4ef7e7bb7d6f44467d2446a2#diff-0fd64f4af511ff3df37c2cbf0f3409917afc250c1fcb8f3df7a8ad2dd9adb3f6)
- 테스트 코드 : [테스트 코드](https://github.com/jissuk/hhplus_eCommerce_java/blob/step17/src/test/java/kr/hhplus/be/server/payment/controller/PaymentControllerTest.java)
  </br> => `결제()`을 통해서 테스트를 진행하였습니다.
