## 아키텍처 설명
이번 과제는 Clean Architecture와 Facade를 사용하여 진행하였습니다.
<br> 도메인 기능 하나를 수행하는 UseCase들을 Facade에서 관리, 조합하는 방식으로 코드를 구현하였으며
<br> 각기 다른 도메인의 로직을 처리해야될 부분은 DomainService에서 처리를 하였습니다.
<br>
## 계층 구조 및 특징
```
Controller -> Facade -> UseCase -> DomainService(선택) -> Repositroy ->  RepositoryImpl(구현체)
```

### Controller
클라이언트로부터의 HTTP 요청을 받아 처리하는 계층입니다. 
<br> `@Valid`을 통해 requestDTO에서 검증을 진행하는 것이 특징입니다.

### Facade
여러 유스케이스를 관리 및 조합하여 기능을 구현하는 계층입니다.
<br> 이번 과제의 경우 주문, 결제, 선착순 쿠폰 발급 기능을 해당 계층에서 구현하였습니다.

### UseCase
한가지 기능 또는 목적에 집중된 서비스 계층 입니다.
<br> 여기서 말하는 한가지 기능은 도메인 기능을 기준으로 삼았으며 포인트 충전을 예시로 들면
<br> 포인트 충전, 포인트내역 등록은 하나의 도메인 기능으로서 유스케이스를 작성하였습니다.

### DomainService(선택)
여러 도메인 로직을 처리하는 도메인 계층입니다. 
<br> 앞서 언급드린대로 이름은 Service이지만 도메인 로직을 처리하는 로직입니다. 이번의 경우 쿠폰을 사용하는 유스케이스에서  <br> 
`쿠폰 사용 여부 확인`, `주문상세의 총금액 계산`, `쿠폰 상태 변경`을 하나의 도메인 서비스 로직으로 묶어서 처리하였습니다.

### Repositroy
도메인이 데이터 베이스 와 상호작용하기 위한 추상화된 계층입니다.
<br> 도메인이 데이터베이스 세부 구현에 의존하지 않도록 하여 결합도를 낮추고, 유연한 구조를 만듭니다.

### Repository(구현체)
RepositoryImpl은 Repository 인터페이스를 실제 데이터베이스 맞게 구현한 구체 클래스입니다.
<br> 인터페이스와 분리되어 있어 데이터 베이스 구현 및 변경 시 도메인이나 상위 계층에 영향을 최소화합니다.


## 프로젝트 패키지 구조 설명
```
📦 kr.hhplus.be.server
├── 📁 common                    ← 공통 유틸, 예외, 응답 등 (전 계층 공유)
│   ├── 📁 annotation            ← 커스텀 어노테이션(DomainService, UseCase)
│   ├── 📁 exception             ← 예외 전역 관리 관련 코드(GlobalExceptionHandler)
│   ├── 📁 response              ← 컨트롤러 응답 객체(HttpStatus, message, data)
│   └── 📁 sender                ← 외부 시스템 연동(추상화)
│
├── 📁 config.jpa               ← JPA 관련 설정 (Infra 지원)
│
├── 📁 external.dataplatform    ← 외부 시스템 연동 (Infra Layer)
│   └── HttpDataSender.java
│── 📁 coupon
    ├── 📁 controller                    ← [Presentation Layer]
    │   └── CouponController.java
    │
    ├── 📁 domain                        ← [Domain Layer]
    │   ├── 📁 mapper                    ← DB → 도메인 변환 등 (도메인 도우미)
    │   ├── 📁 model                     ← 순수 도메인 모델 (Entity 아님)
    │   ├── 📁 repository                ← 저장소 인터페이스 (도메인 중심)
    │   └── 📁 service                   ← 도메인 서비스 (비즈니스 규칙 응집)
    │
    ├── 📁 exception                     ← [도메인 / 유스케이스 에러 정의]
    │   ├── CouponNotFoundException.java
    │   ├── CouponOperationException.java
    │   ├── CouponOutOfStockException.java
    │   ├── InvalidCouponException.java
    │   └── UserCouponNotFoundException.java
    │
    ├── 📁 facade                        ← [Application Layer / 유스케이스 조합]
    │   └── CouponFacade.java
    │
    ├── 📁 infrastructure                ← [Infrastructure Layer]
    │   ├── CouponRepositoryImpl.java     ← 구현체
    │   └── UserCouponRepositoryImpl.java
    │
    └── 📁 usecase                       ← [UseCase Layer]
        ├── 📁 dto                       ← DTO 객체들
        ├── CheckCouponStockUseCase.java
        ├── RegisterUserCouponUseCase.java
        └── UseCouponUseCase.java

```
### 클린 코드 작성을 위해 신경쓴 부분
#### - 도메인별로 상위 커스텀 예외를 정의하고, 그 하위에 세부 예외들을 계층적으로 구성했습니다.
이러한 구조는 각 도메인의 책임을 명확하게 분리하고, 예외 확장 시에도 변경 없이 쉽게 확장이 가능합니다.

<br>

#### - 입력 값 검증에 `@Valid`를 사용하였습니다.
`@Valid` 는 검증 로직을 컨트롤러나 서비스에서 진행하지 않고 DTO에서 진행함으로서 책임을 명확히 하였습니다.

<br>

#### - `MapStruct`를 사용해 `DTO`와 도메인 객체 간 변환 로직을 변경하였습니다.
`ModelMapper`와 같이 `MapStruct`는 간단하게 객체간의 변환이 가능하여 중복된 코드 작성을 지양하게 됩니다.
<br> 이를 통해 변환 코드를 명확히 분리하고, 중복과 오류 가능성을 줄이며, 핵심 비즈니스 로직에 집중할 수 있게 되었습니다.
<br> +) `ModelMapper`는 비교적 간단하게 사용할 수 있지만 속도면에서 불리한 부분이 있기에 초기에는 `ModelMapper`로 작성하였지만
<br> 자주 사용되는 기능인 만큼 성능 상 이점이 있는 `MapStruct`로 리펙토링을 진행하였습니다. 

<br>

### - 전역 예외 처리(GlobalExceptionHandler)를 도입했습니다.
예외 처리 로직을 한 곳에 모아 책임을 분리하고, 중복 코드를 제거하며 일관된 에러 응답을 제공하였습니다.

<br>

#### - 테스트 클래스에 `@Nested`를 사용하였습니다.
`@Nested`는 상황, 기능 별로 계층화를 할 수 있으며 이번의 경우 성공과 실패 케이스를 계층화 하여 가독성을 향상 시켰습니다. 

<br>

#### - 테스트용 `Step` 클래스를 만들어 기본값이 설정된 객체 생성을 담당하게 했습니다
`Step` 클래스을 통해 테스트 코드의 중복을 줄이고, 가독성을 높이며, 테스트 목적을 명확하게 하였습니다.
<br> 또한, 테스트 데이터 준비 책임을 분리하여 테스트 코드의 유지보수성과 재사용성을 크게 개선하였습니다.



