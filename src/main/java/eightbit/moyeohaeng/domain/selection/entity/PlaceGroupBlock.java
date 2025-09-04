package eightbit.moyeohaeng.domain.selection.entity;

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
	name = "place_group_blocks",
	uniqueConstraints = @UniqueConstraint(columnNames = {"place_block_id", "place_group_id"}),
	indexes = {
		@Index(name = "idx_pgb_place_block_id", columnList = "place_block_id"),
		@Index(name = "idx_pgb_place_group_id", columnList = "place_group_id")
	}
)
public class PlaceGroupBlock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_block_id", nullable = false)
	private PlaceBlock placeBlock;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_group_id", nullable = false)
	private PlaceGroup placeGroup;

	public static PlaceGroupBlock of(PlaceGroup placeGroup, PlaceBlock placeBlock) {
		return builder()
			.placeGroup(placeGroup)
			.placeBlock(placeBlock)
			.build();
	}
}
