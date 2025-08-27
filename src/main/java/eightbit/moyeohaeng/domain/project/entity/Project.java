package eightbit.moyeohaeng.domain.project.entity;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

	// TODO: Replace with actual relationship once Team entity is created
	@Column(name = "team_id")
	private Long teamId;

	@NotBlank
	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "external_id", nullable = false, length = 36, unique = true)
	private String externalId;

	@Enumerated(EnumType.STRING)
	@Column(name = "project_access", nullable = false)
	private ProjectAccess projectAccess;

	@Column(name = "location")
	private String location;

	@Column(name = "start_date", nullable = true)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

}
