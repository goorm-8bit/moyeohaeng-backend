package eightbit.moyeohaeng.domain.project.entity;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
	name = "pin",
	indexes = @Index(name = "idx_pin_project_id", columnList = "project_id")
)
@Check(constraints = "latitude BETWEEN -90 AND 90 AND longitude BETWEEN -180 AND 180")
@SQLDelete(sql = "UPDATE pin SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Pin extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 200)
	private String address;

	@DecimalMin("-90.0")
	@DecimalMax("90.0")
	@Column(nullable = true)
	private Double latitude;

	@DecimalMin("-180.0")
	@DecimalMax("180.0")
	@Column(nullable = true)
	private Double longitude;

	@Column(name = "detail_link", length = 255)
	private String detailLink;

	@Column(length = 30)
	private String category;

	@Column(length = 120)
	private String author;

	@Column(name = "project_id", nullable = false)
	private Long projectId;
}
