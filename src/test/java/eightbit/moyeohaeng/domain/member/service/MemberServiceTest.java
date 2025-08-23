package eightbit.moyeohaeng.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import eightbit.moyeohaeng.domain.member.dto.request.MemberRegisterRequest;
import eightbit.moyeohaeng.domain.member.dto.request.MemberUpdateRequest;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.exception.MemberException;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@DisplayName("회원 생성 테스트")
	@Test
	void create() {
		// given
		MemberRegisterRequest request = new MemberRegisterRequest("test@test.com", "password", "nickname");
		Member member = request.toEntity();
		given(memberRepository.save(any(Member.class))).willReturn(member);

		// when
		Member newMember = memberService.create(request);

		// then
		assertThat(newMember.getEmail()).isEqualTo(request.email());
		assertThat(newMember.getName()).isEqualTo(request.name());
	}

	@DisplayName("회원 정보 수정 테스트 - 성공")
	@Test
	void update_success() {
		// given
		Long memberId = 1L;
		Member existingMember = Member.builder()
			.email("test@test.com")
			.name("oldNickname")
			.password("password")
			.build();
		MemberUpdateRequest request = new MemberUpdateRequest("newNickname", "newProfileImg", "newPassword");

		given(memberRepository.findById(memberId)).willReturn(Optional.of(existingMember));

		// when
		memberService.update(memberId, request);

		// then
		assertThat(existingMember.getName()).isEqualTo("newNickname");
		assertThat(existingMember.getProfileImage()).isEqualTo("newProfileImg");
	}

	@DisplayName("회원 정보 수정 테스트 - 실패 (회원 없음)")
	@Test
	void update_fail_memberNotFound() {
		// given
		Long memberId = 1L;
		MemberUpdateRequest request = new MemberUpdateRequest("newNickname", "newProfileImg", "newPassword");
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		MemberException exception = assertThrows(MemberException.class, () -> {
			memberService.update(memberId, request);
		});

		assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@DisplayName("ID로 회원 조회 테스트 - 성공")
	@Test
	void findById_success() {
		// given
		Long memberId = 1L;
		Member member = Member.builder()
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
		MemberException exception = assertThrows(MemberException.class, () -> {
			memberService.findById(memberId);
		});

		assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
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
		MemberException exception = assertThrows(MemberException.class, () -> {
			memberService.findByEmail(email);
		});

		assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@DisplayName("회원 삭제 테스트 - 성공")
	@Test
	void delete_success() {
		// given
		Long memberId = 1L;
		Member member = Member.builder()
			.email("test@test.com")
			.name("nickname")
			.password("password")
			.build();
		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

		// when
		assertDoesNotThrow(() -> memberService.delete(memberId));

		// then
		then(memberRepository).should(times(1)).delete(member);
	}

	@DisplayName("회원 삭제 테스트 - 실패 (회원 없음)")
	@Test
	void delete_fail_memberNotFound() {
		// given
		Long memberId = 1L;
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		MemberException exception = assertThrows(MemberException.class, () -> {
			memberService.delete(memberId);
		});

		assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}
}
