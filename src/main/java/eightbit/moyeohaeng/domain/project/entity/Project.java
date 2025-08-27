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
	private Long teamId;

	@NotBlank
	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 36, unique = true)
	private String externalId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProjectAccess projectAccess;

	private String location;

	@Column(nullable = true)
	private LocalDate startDate;

	@Column(nullable = true)
	private LocalDate endDate;

}
