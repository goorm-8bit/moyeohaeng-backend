package eightbit.moyeohaeng.domain.team.entity;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	name = "team_members",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_team_member", columnNames = {"team_id", "member_id"})
	}
)

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE team_members SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class TeamMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//	@Column(name = "team_member_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private TeamRole teamRole;

	@Builder
	private TeamMember(Team team, Member member, TeamRole teamRole) {
		this.team = team;
		this.member = member;
		this.teamRole = teamRole;
	}

	public static TeamMember of(Team team, Member member) {
		return TeamMember.builder()
			.team(team)
			.member(member)
			.teamRole(TeamRole.OWNER)
			.build();
	}
}
