package eightbit.moyeohaeng.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.member.dto.MemberDto;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.exception.MemberException;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public Member create(MemberDto.RegisterRequest request) {

		Member newMember = request.toEntity();
		return memberRepository.save(newMember);
	}

	@Transactional
	public MemberDto.Info update(Long memberId, MemberDto.UpdateRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		// member.update(request.name(), request.profileImage());

		Member updatedMember = member.copy(request);

		memberRepository.save(updatedMember);

		return MemberDto.Info.from(updatedMember);
	}

	public MemberDto.Info findById(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		return MemberDto.Info.from(member);
	}

	public MemberDto.Info findByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		return MemberDto.Info.from(member);
	}

	@Transactional
	public void delete(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		memberRepository.delete(member);
	}
}
