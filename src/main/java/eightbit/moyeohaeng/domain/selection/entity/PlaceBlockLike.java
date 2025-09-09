package eightbit.moyeohaeng.domain.selection.entity;

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
import jakarta.persistence.UniqueConstraint;
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
	name = "place_block_likes",
	uniqueConstraints = @UniqueConstraint(columnNames = {"author", "place_block_id"}),
	indexes = {
		@Index(name = "idx_pbl_place_block_id", columnList = "place_block_id"),
		@Index(name = "idx_pbl_author", columnList = "author")
	}
)
@SQLDelete(sql = "UPDATE place_block_likes SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class PlaceBlockLike extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "author", nullable = false, length = 150)
	private String author;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_block_id", nullable = false)
	private PlaceBlock placeBlock;

	/**
	 * 좋아요 생성 팩토리 메서드
	 *
	 * @param author     작성자 이메일
	 * @param placeBlock 대상 장소 블록
	 * @return 생성된 좋아요 엔티티
	 */
	public static PlaceBlockLike of(String author, PlaceBlock placeBlock) {
		return PlaceBlockLike.builder()
			.author(author)
			.placeBlock(placeBlock)
			.build();
	}
}
