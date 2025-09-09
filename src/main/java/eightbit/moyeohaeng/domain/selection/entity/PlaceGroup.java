package eightbit.moyeohaeng.domain.selection.entity;

import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(
	name = "place_groups",
	indexes = {
		@Index(name = "idx_place_groups_project", columnList = "project_id")
	}
)
public class PlaceGroup extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "color", nullable = false, length = 7)
	private String color;

	@Column(name = "memo", length = 14)
	private String memo;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	public static PlaceGroup of(String name, String color, Project project) {
		return builder()
			.name(name)
			.color(color)
			.project(project)
			.build();
	}

	public void update(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void updateMemo(String memo) {
		this.memo = memo;
	}
}
