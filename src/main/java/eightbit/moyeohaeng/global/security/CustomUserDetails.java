package eightbit.moyeohaeng.global.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;

	private CustomUserDetails(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.authorities = authorities;
	}

	public static CustomUserDetails from(Member member) {
		return new CustomUserDetails(
			member.getId(),
			member.getEmail(),
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
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
}
