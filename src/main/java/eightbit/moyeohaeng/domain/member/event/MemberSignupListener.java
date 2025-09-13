package eightbit.moyeohaeng.domain.member.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberSignupListener {

	private final MemberRepository memberRepository;
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;

	/**
	 * 회원가입 이벤트를 처리하여 개인 팀을 생성하고 회원을 소유자로 등록
	 * - 회원가입 트랜잭션이 완료된 후 실행 (AFTER_COMMIT)
	 * - 새로운 트랜잭션에서 실행 (REQUIRES_NEW)
	 * - 트랜잭션이 없는 경우에도 실행 (fallbackExecution)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
	public void handleMemberSignedUp(MemberSignedUpEvent event) {
		try {
			Member member = findMemberById(event.memberId());
			createPersonalTeamWithOwner(member);
		} catch (Exception e) {
			log.error("회원의 개인 팀 생성 실패. 회원 ID: {}", event.memberId(), e);
		}
	}

	/**
	 * 회원 ID로 회원 정보를 조회
	 * @throws IllegalStateException 회원을 찾을 수 없는 경우 (회원가입 이벤트와 실제 회원 생성 사이의 비정합성 발생 시)
	 */
	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalStateException("회원가입 이벤트 처리 중 회원을 찾을 수 없음. ID: " + memberId));
	}

	/**
	 * 회원의 개인 팀을 생성하고 해당 회원을 팀의 소유자로 등록
	 * 1. 회원이 이미 다른 팀의 소유자인 경우 중복 생성 방지
	 * 2. 회원의 이메일을 팀 이름으로 사용하여 개인 팀 생성
	 * 3. 생성된 팀에 대해 회원을 소유자(OWNER)로 등록
	 */
	private void createPersonalTeamWithOwner(Member member) {
		// 1. 이미 다른 팀의 소유자인지 확인
		boolean isAlreadyOwner = teamMemberRepository.findByMember_IdAndDeletedAtIsNull(member.getId()).stream()
			.anyMatch(tm -> tm.getTeamRole() == TeamRole.OWNER);

		if (isAlreadyOwner) {
			log.info("회원이 이미 개인 팀을 보유 중: {}", member.getEmail());
			return;
		}

		// 2. 개인 팀 생성
		Team personalTeam = Team.of(member.getEmail());
		teamRepository.save(personalTeam);

		// 3. 팀 소유자 등록
		TeamMember ownerMembership = TeamMember.of(personalTeam, member);
		teamMemberRepository.save(ownerMembership);
	}
}
