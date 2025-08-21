## java doc
1. public 메서드에는 전부 사용
2. get, set과 같은 간단한 것은 제외

---
## Test 코드

1. 단위 테스트 
- Service와 utill만 테스트 진행
1. 통합 테스트
- docker를 사용해서 DB와 redis 생성

---

## DTO 규칙

1. 항상 recode를 사용

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
```
---
## 테이블 명
```java

@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @PrePersist
    // ...
}
```
---
## 빌더 패턴

1. 빌더 어노테이션 사용

```java

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {
// ... 
}
```

1. DTO나 Entity 안에 정적 팩토리 메서드 사용 (서비스단에 코드 수를 줄이기 위해)

```java
public static Order createNewOrder(Member member, Store store, String orderNumber, Long totalPrice,  OrderCreateRequest request) {
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
---
## Versioning

- @RequestMapping 맨 앞에 버전 붙이기 (`/v1`)

```java
@RequestMapping("/v1/booking/reservation")
public class ReservationController {
}
```
---
## Interface

1. controller 필수 사용
    1. swagger문서를 작성하기 위해서 
    2. swagger를 위한 interface 파일에 api를 붙임 ex) UserApi
    3.  baseApi를 상속받아서 작성 선택적 사용 
    [https://velog.io/@juuuunny/Controller-레이어와-문서화-Swagger-OpenAPI-어노테이션-분리-전략](https://velog.io/@juuuunny/Controller-%EB%A0%88%EC%9D%B4%EC%96%B4%EC%99%80-%EB%AC%B8%EC%84%9C%ED%99%94-Swagger-OpenAPI-%EC%96%B4%EB%85%B8%ED%85%8C%EC%9D%B4%EC%85%98-%EB%B6%84%EB%A6%AC-%EC%A0%84%EB%9E%B5)
2. service에서는 선택적 사용
    1. 사용하는 이유에 대해서 자세히 설명하기
