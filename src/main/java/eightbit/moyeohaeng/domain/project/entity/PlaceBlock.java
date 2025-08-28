package eightbit.moyeohaeng.domain.project.entity;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;

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

/**
 * 프로젝트의 지도 보드에 찍힌 핀/장소 블록(PlaceBlock)을 표현하는 엔티티.
 * Soft delete 대상으로, 좌표 및 메타정보(메모/링크/일정/색상 등)을 포함한다.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "place_blocks",
	indexes = {
		@Index(name = "idx_place_blocks_project", columnList = "project_id")
	}
)
@Check(constraints = "latitude BETWEEN -90 AND 90 AND longitude BETWEEN -180 AND 180")
@SQLDelete(sql = "UPDATE place_blocks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class PlaceBlock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@Column(name = "memo", length = 50)
	private String memo;

	@Column(name = "detail_link", length = 250)
	private String detailLink;

	@Column(name = "category", length = 30)
	private String category;

	@Column(name = "color", length = 20)
	private String color;

	@Column(name = "author", length = 120)
	private String author;

	/**
	 * 특정 프로젝트에 새로운 PlaceBlock 엔티티를 생성하는 정적 팩토리 메서드.
	 * 필수 정보(이름, 좌표 등)와 함께 블록의 타입(PIN, PLACE_BLOCK)을 지정합니다.
	 */
	public static PlaceBlock of(
		Project project, String name, Double latitude, Double longitude, String author
	) {
		return PlaceBlock.builder()
			.project(project).name(name)
			.latitude(latitude).longitude(longitude)
			.author(author)
			.build();
	}

	public void update(
		String name, String address, Double latitude, Double longitude,
		String memo, String detailLink, String category, String color, String author) {

		if (name != null)
			this.name = name;
		if (address != null)
			this.address = address;
		if (latitude != null)
			this.latitude = latitude;
		if (longitude != null)
			this.longitude = longitude;
		if (memo != null)
			this.memo = memo;
		if (detailLink != null)
			this.detailLink = detailLink;
		if (category != null)
			this.category = category;
		if (color != null)
			this.color = color;
		if (author != null)
			this.author = author;
	}
}
