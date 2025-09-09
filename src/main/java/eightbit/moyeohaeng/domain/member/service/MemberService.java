package eightbit.moyeohaeng.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.member.common.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.common.exception.MemberException;
import eightbit.moyeohaeng.domain.member.dto.request.MemberUpdateRequest;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public void update(Long memberId, MemberUpdateRequest request) {
		Member member = findById(memberId);
		member.update(request.name(), request.profileImage(), request.password());
	}

	public Member findById(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		return member;
	}

	public Member findByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		return member;
	}

	@Transactional
	public boolean delete(Long memberId) {
		Member member = findById(memberId);

		if (member.isDeleted()) {
			return false;
		}
		memberRepository.delete(member);

		return true;
	}
}
