package eightbit.moyeohaeng.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import eightbit.moyeohaeng.domain.member.common.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.common.exception.MemberException;
import eightbit.moyeohaeng.domain.member.dto.request.MemberUpdateRequest;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@DisplayName("회원 정보 수정 테스트 - 성공")
	@Test
	void update_success() {
		// given
		Long memberId = 1L;
		Member existingMember = Member.builder()
			.id(memberId)
			.email("test@test.com")
			.name("oldNickname")
			.password("password")
			.build();
		MemberUpdateRequest request = new MemberUpdateRequest("newNickname", "newProfileImg", "newPassword");

		given(memberRepository.findById(memberId)).willReturn(Optional.of(existingMember));

		// when & then
		assertThatCode(() -> memberService.update(memberId, request))
			.doesNotThrowAnyException();
	}

	@DisplayName("회원 정보 수정 테스트 - 실패 (회원 없음)")
	@Test
	void update_fail_memberNotFound() {
		// given
		Long memberId = 1L;
		MemberUpdateRequest request = new MemberUpdateRequest("newNickname", "newProfileImg", "newPassword");
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.update(memberId, request))
			.isInstanceOf(MemberException.class)
			.hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
	}

	@DisplayName("ID로 회원 조회 테스트 - 성공")
	@Test
	void findById_success() {
		// given
		Long memberId = 1L;
		Member member = Member.builder()
			.id(memberId)
			.email("test@test.com")
			.name("nickname")
			.password("password")
			.build();
		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

		// when
		Member foundMember = memberService.findById(memberId);

		// then
		assertThat(foundMember).isEqualTo(member);
	}

	@DisplayName("ID로 회원 조회 테스트 - 실패 (회원 없음)")
	@Test
	void findById_fail_memberNotFound() {
		// given
		Long memberId = 1L;
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.findById(memberId))
			.isInstanceOf(MemberException.class)
			.hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
	}

	@DisplayName("Email로 회원 조회 테스트 - 성공")
	@Test
	void findByEmail_success() {
		// given
		String email = "test@test.com";
		Member member = Member.builder()
			.email(email)
			.name("nickname")
			.password("password")
			.build();
		given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

		// when
		Member foundMember = memberService.findByEmail(email);

		// then
		assertThat(foundMember).isEqualTo(member);
	}

	@DisplayName("Email로 회원 조회 테스트 - 실패 (회원 없음)")
	@Test
	void findByEmail_fail_memberNotFound() {
		// given
		String email = "test@test.com";
		given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.findByEmail(email))
			.isInstanceOf(MemberException.class)
			.hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
	}

	@DisplayName("회원 삭제 테스트 - 성공")
	@Test
	void delete_success() {
		// given
		Long memberId = 1L;
		Member member = Member.builder()
			.id(memberId)
			.email("test@test.com")
			.name("nickname")
			.password("password")
			.build();
		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

		// when & then
		assertThatCode(() -> memberService.delete(memberId))
			.doesNotThrowAnyException();
	}

	@DisplayName("회원 삭제 테스트 - 실패 (회원 없음)")
	@Test
	void delete_fail_memberNotFound() {
		// given
		Long memberId = 1L;
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.delete(memberId))
			.isInstanceOf(MemberException.class)
			.hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
	}
}
