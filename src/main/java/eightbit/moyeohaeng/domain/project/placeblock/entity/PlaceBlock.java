package eightbit.moyeohaeng.domain.project.placeblock.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import eightbit.moyeohaeng.domain.project.placeblock.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프로젝트의 지도 보드에 찍힌 핀/장소 블록(PlaceBlock)을 표현하는 엔티티.
 * Soft delete 대상으로, 좌표 및 메타정보(메모/링크/일정/색상 등)을 포함한다.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
	name = "place_blocks",
	indexes = {
		@Index(name = "idx_place_blocks_project", columnList = "project_id")
	}
)
@Check(constraints = "latitude BETWEEN -90 AND 90 AND longitude BETWEEN -180 AND 180")
@SQLDelete(sql = "UPDATE place_blocks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PlaceBlock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Setter(AccessLevel.NONE)
	private Long id;

	@NotNull
	@Column(name = "project_id", nullable = false)
	@Setter(AccessLevel.NONE)
	private Long projectId;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	@Size(max = 255)
	@Column(length = 255)
	private String address;

	@NotNull
	@DecimalMin("-90.0")
	@DecimalMax("90.0")
	@Column(nullable = false)
	private Double latitude;

	@NotNull
	@DecimalMin("-180.0")
	@DecimalMax("180.0")
	@Column(nullable = false)
	private Double longitude;

	@Size(max = 2000)
	@Column(length = 2000)
	private String memo;

	private LocalDate date;
	private LocalTime time;

	@Size(max = 255)
	@Column(length = 255)
	private String reviewLink;

	@Size(max = 255)
	@Column(length = 255)
	private String detailLink;

	@Size(max = 50)
	@Column(length = 50)
	private String category;

	@Size(max = 20)
	@Column(length = 20)
	private String color;

	@Size(max = 120)
	@Column(length = 120)
	private String author;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@NotNull
	private PlaceBlockType type;

	/**
	 * 특정 프로젝트에 새로운 PlaceBlock 엔티티를 생성하는 정적 팩토리 메서드.
	 * 필수 정보(이름, 좌표 등)와 함께 블록의 타입(PIN, PLACE_BLOCK)을 지정합니다.
	 */
	public static PlaceBlock of(
		Long projectId, String name, Double latitude, Double longitude,
		String author, PlaceBlockType type
	) {
		return PlaceBlock.builder()
			.projectId(projectId).name(name)
			.latitude(latitude).longitude(longitude)
			.author(author).type(type)
			.build();
	}

	public void apply(PlaceBlockUpdateRequest req) {
		if (req.name() != null)
			this.name = req.name();
		if (req.address() != null)
			this.address = req.address();
		if (req.latitude() != null)
			this.latitude = req.latitude();
		if (req.longitude() != null)
			this.longitude = req.longitude();
		if (req.memo() != null)
			this.memo = req.memo();
		if (req.date() != null)
			this.date = req.date();
		if (req.time() != null)
			this.time = req.time();
		if (req.reviewLink() != null)
			this.reviewLink = req.reviewLink();
		if (req.detailLink() != null)
			this.detailLink = req.detailLink();
		if (req.category() != null)
			this.category = req.category();
		if (req.color() != null)
			this.color = req.color();
		if (req.author() != null)
			this.author = req.author();
		if (req.type() != null)
			this.type = req.type();
	}
}