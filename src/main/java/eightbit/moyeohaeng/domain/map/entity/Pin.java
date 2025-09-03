package eightbit.moyeohaeng.domain.map.entity;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.place.entity.Place;
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
	name = "pins",
	indexes = @Index(name = "idx_pin_project_id", columnList = "project_id")
)
@SQLDelete(sql = "UPDATE pins SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Pin extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "author")
	private String author;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;
}
