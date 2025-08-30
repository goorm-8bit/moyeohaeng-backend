package eightbit.moyeohaeng.domain.project.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.team.entity.Team;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "projects")
@SQLDelete(sql = "UPDATE projects SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Project extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@Column(name = "external_id", nullable = false, length = 36, unique = true)
	private String externalId;

	@Enumerated(EnumType.STRING)
	@Column(name = "project_access", nullable = false)
	private ProjectAccess projectAccess;

	@Column(name = "location", length = 50)
	private String location;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	// 프로젝트 생성자 (소유자)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "creator_id", nullable = false)
	private Member creator;

	// 프로젝트 생성자 설정 메소드
	public void setCreator(Member creator) {
		this.creator = Objects.requireNonNull(creator, "creator는 null일 수 없습니다.");
	}

	// 특정 사용자가 프로젝트 소유자인지 확인
	public boolean isOwnedBy(Member member) {
		return this.creator != null && member != null
			&& Objects.equals(this.creator.getId(), member.getId());
	}
}
