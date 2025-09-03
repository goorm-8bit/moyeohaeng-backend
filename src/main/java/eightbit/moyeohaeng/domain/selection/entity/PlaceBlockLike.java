package eightbit.moyeohaeng.domain.selection.entity;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.global.domain.BaseEntity;
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
	uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "place_block_id"}),
	indexes = {
		@Index(name = "idx_pbl_place_block_id", columnList = "place_block_id"),
		@Index(name = "idx_pbl_member_id", columnList = "member_id")
	}
)
@SQLDelete(sql = "UPDATE place_block_likes SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class PlaceBlockLike extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_block_id", nullable = false)
	private PlaceBlock placeBlock;
}
