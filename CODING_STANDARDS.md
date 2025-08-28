# ✨ 코드 컨벤션 가이드

이 문서는 프로젝트의 **일관성**과 **가독성**을 높이기 위한 코드 작성 규칙을 담고 있어요.

기본적인 규칙은 [네이버 핵데이](https://naver.github.io/hackday-conventions-java/)를 따르며, 이 문서에 없는 내용은 네이버 핵데이 규칙을 준수해 주세요. 추가 의견이 있다면
언제든지 공유해 주세요\!

-----

## 📚 Javadoc

- 필수 사용 ✔️: 함수명이나 클래스명만으로 동작을 예측하기 어려운 **복잡한 public 메서드**에는 반드시 사용
- 사용 지양 ❌: `get`, `set`과 같이 역할이 명확한 메서드나 간단한 클래스에는 사용X

<예시>

```java
/**
 * 주어진 주문 번호로 주문 정보를 조회하고,
 * 주문 상태에 따라 다른 메시지를 반환합니다.
 *
 * @param orderId 조회할 주문의 고유 ID
 * @return 주문 상태에 대한 상세 메시지 (예: "주문이 정상적으로 처리되었습니다.")
 * @throws OrderNotFoundException 주문 ID에 해당하는 주문이 없을 경우 발생
 */
public String getOrderStatusMessage(Long orderId) {
	// ... 구현 내용
}
```

-----

## 🗃️ Entity

1. **기본키 & 외래키 이름 규칙** 🔑
    * 기본키는 `id`로 통일합니다.
    * 외래키는 **테이블명**과 `_id`를 결합한 형태로 사용합니다. (예: `PlaceBlock`의 외래키 → `place_block_id`)
2. **`@Column` 필수 사용** 🏷️
    * 모든 필드에 `@Column` 어노테이션을 사용하고 `name` 속성을 명시합니다. 이를 통해 필드명과 컬럼명의 불일치로 인한 혼란을 방지할 수 있습니다.
3. **`@OneToMany` 사용 지양** 🚫
    * `@OneToMany`는 사용하지 않습니다. 대신 Repository에 별도의 메서드를 만들어 관계를 구현합니다. 이는 **N+1 문제**를 방지하고, 서비스 레이어에서 필요한 데이터만 효율적으로 조회하기
      위함입니다.
4. **`@onDelete` 활용** ♻️
    * `@OneToMany`의 대체 기능으로 `@onDelete`를 사용해 부모 Entity가 삭제될 때 연관된 자식 Entity의 처리 방식을 명확히 정의합니다.
5. **테이블 이름은 복수형** 👥
    * `@Table(name = "orders")`와 같이 테이블 이름은 항상 복수형을 사용합니다.

-----

## 📊 Repository

1. `@Repository` 어노테이션을 **반드시** 명시적으로 사용합니다.
    * Spring에서는 `@Repository`가 선택적이지만, 우리 프로젝트에서는 **계층의 역할**을 명확히 구분하기 위해 사용합니다.

-----

## 🧩 Service

1. **인터페이스 사용은 선택 (단, 사용시 명확한 이유 필요)

-----

## 🌐 Controller

- **Swagger 전용 인터페이스 사용** 📝
    - Swagger 문서는 `UserApi`와 같이 `Api`를 이름에 포함하는 전용 인터페이스를 사용해 작성합니다. 이는 Controller의 비즈니스 로직과 문서화 로직을 분리하여 코드를 깔끔하게 유지하기
      위함
    - 자세한
      내용은 [Swagger 문서화 가이드](https://github.com/goorm-8bit/moyeohaeng-backend/wiki/%F0%9F%93%9D-Swagger-%EB%AC%B8%EC%84%9C%ED%99%94-%EA%B0%80%EC%9D%B4%EB%93%9C)
      를 참고하세요.

-----

## 📦 DTO

1. **`record` 사용** 🆕
    - DTO는 불변 객체인 `record`를 사용합니다. `record`는 `equals()`, `hashCode()`, `toString()` 등을 자동으로 생성합니다.
2. **`@Schema` 사용** ✍️
    - **Swagger** 문서화를 위해 `@Schema`를 사용해 필드에 대한 설명과 예시를 명시

<예시>:

```java

@Schema(description = "회원 정보 응답 DTO")
public record MemberInfoResponse(

	@Schema(description = "회원 이름", example = "홍길동")
	String name,

	@Schema(description = "회원 연락처", example = "01012345678")
	String phoneNumber,

	@Schema(description = "회원 역할", example = "MEMBER")
	Role role,

	@Schema(description = "회원 상세 정보")
	MemberDetailInfo memberDetailInfo
) {
	// memberDetail -> memberDetailInfo 변환 후 MemberInfoResponse 생성
	public static MemberInfoResponse of(String name, String phoneNumber, MemberDetail memberDetail) {

		MemberDetailInfo memberDetailInfo = new MemberDetailInfo(
			memberDetail.getMemberNo(),
			memberDetail.getMembership(),
			memberDetail.getEmail(),
			memberDetail.getBirthDate(),
			memberDetail.getGender(),
			memberDetail.getTotalMileage()
		);

		return new MemberInfoResponse(name, phoneNumber, Role.MEMBER, memberDetailInfo);
	}
}
```

-----

## 🛠️ 빌더 패턴

1. **`@Builder` 어노테이션 사용
    - `@Builder`와 함께 `@AllArgsConstructor`와 `@NoArgsConstructor`를 사용하여 빌더 패턴을 구현
    - 접근 제어자를 `private` 또는 `protected`로 설정하여 외부에서의 직접적인 객체 생성을 방지

<예시>:

```java

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {
	// ... 필드
}
```

2. **정적 팩토리 메서드 활용** 🏭
    - DTO나 Entity 내부에 정적 팩토리 메서드를 만들어 객체 생성 로직을 캡슐화합니다.
    - 이는 서비스 레이어의 코드 길이를 줄여주는 효과가 있습니다.

<예시>

```java
public static Order createNewOrder(Member member, Store store, String orderNumber, Long totalPrice,
	OrderCreateRequest request) {
	return Order.builder()
		.member(member)
		.store(store)
		.orderNumber(orderNumber)
		.totalPrice(totalPrice)
		.status(OrderStatus.PLACED)
		.pickupType(request.pickupType())
		.requestMemo(request.requestMemo())
		.expectedPickupTime(java.time.LocalDateTime.now().plusMinutes(10))
		.cardNumber(request.cardNumber())
		.build();
}
```

-----

## 🔢 Versioning

1. `@RequestMapping`에 API 버전을 명시합니다. (예: `/v1`)

<예시>

```java

@RequestMapping("/v1/booking/reservation")
public class ReservationController {
	// ...
}
```

## 테스트

1. 단위 테스트
    - Service와 utill만 테스트 진행
2. 통합 테스트
    - docker를 사용해서 DB와 redis 생성
