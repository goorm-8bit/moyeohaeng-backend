## java doc
1. 모든 public 메서드/클래스/인터페이스/enum에 Javadoc 작성
2. 단순 getter/setter(또는 Lombok이 생성한 메서드)는 제외
3. 태그 사용: `@param`, `@return`, `@throws` 필수. 외부에 공개되는 API는 `@since`, `@deprecated` 권장
4. 예외 흐름, 스레드-세이프티, 부작용(side effects), 시간/타임존 의존성도 문서화

---
## Test 코드

1. 단위 테스트 
- Service/Util 위주로 작성하되, 도메인 모델과 리포지토리 슬라이스(@DataJpaTest)도 포함
- 컨트롤러는 @WebMvcTest 또는 RestAssured로 API 계약 테스트(성공/실패 케이스, 예외 매핑)
1. 통합 테스트
- Testcontainers로 DB/Redis 컨테이너 기동(로컬 Docker 의존 최소화, CI 호환성 ↑)

---
## DTO 규칙

1. 항상 record를 사용 (DTO에 한정). JPA Entity에는 record 사용 금지
2. record 사용을 전제로 최소 JDK 17 이상 사용 확인 필요

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

	// ...
}
```
---
## 테이블 명
단수가 아닌 복수로 사용
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

- 시간 의존 로직은 `Clock` 주입 또는 파라미터로 받는다.
- 결제수단 등 민감정보는 저장 금지 또는 KMS/암호화/토큰화 정책을 따른다.

```java
public static Order createNewOrder(
        Member member,
        Store store,
        String orderNumber,
        Long totalPrice,
        OrderCreateRequest request,
        LocalDateTime expectedPickupTime
) {
        return Order.builder()
                .member(member)
                .store(store)
                .orderNumber(orderNumber)
                .totalPrice(totalPrice)
                .status(OrderStatus.PLACED)
                .pickupType(request.pickupType())
                .requestMemo(request.requestMemo())
                .expectedPickupTime(expectedPickupTime)
                // cardNumber 저장 시 암호화/토큰화 정책을 사전에 정의하고 적용
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
