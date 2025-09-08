package eightbit.moyeohaeng.global.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import lombok.Getter;

/**
 * 인증된 사용자의 정보를 Spring Security에 제공하는 클래스입니다. 사용자 인증 후 SecurityContext에 저장되어
 * 권한 검사 및 인증 정보 접근에 활용됩니다.
 * <p>
 * 이 클래스는 다음과 같은 상황에서 사용됩니다:
 * <ul>
 *   <li>JWT 토큰 기반 인증 시 토큰에서 사용자 정보 추출 후 인증 객체 생성에 사용</li>
 * </ul>
 * <p>
 * Spring Security의 {@code UserDetails} 구현체로, 인증된 회원의 식별자와 권한 정보를 제공합니다.
 * 불변(immutable) 설계로 스레드 세이프합니다.
 *
 * @since 0.1.0
 */
@Getter
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final String name;
	private final String email;
	private final String profileImage;
	private final Collection<? extends GrantedAuthority> authorities;
	private final UserType userType;

	private CustomUserDetails(Long id, String name, String email, String profileImage,
		Collection<? extends GrantedAuthority> authorities, UserType userType) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.profileImage = profileImage;
		this.authorities = authorities;
		this.userType = userType;
	}

	/**
	 * Member 엔티티로부터 {@code CustomUserDetails}를 생성합니다.
	 *
	 * @param member 로그인 대상 회원 (null 불가)
	 * @return 생성된 {@code CustomUserDetails}
	 * @throws NullPointerException {@code member}가 null인 경우
	 */
	public static CustomUserDetails from(Member member) {
		Objects.requireNonNull(member, "Member는 null일 수 없습니다.");
		return new CustomUserDetails(
			member.getId(),
			member.getName(),
			member.getEmail(),
			member.getProfileImage(),
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
			UserType.MEMBER
		);
	}

	/**
	 * Create a guest/viewer principal from share token context.
	 */
	public static CustomUserDetails guestOf(UserType userType) {
		return new CustomUserDetails(
			null,
			null,
			null,
			null,
			Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userType.name())),
			userType
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		if (isGuest()) {
			return userType.name();
		}
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getMemberId() {
		return id;
	}

	public boolean isGuest() {
		return UserType.MEMBER != userType;
	}
}
