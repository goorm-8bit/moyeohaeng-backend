package eightbit.moyeohaeng.domain.project.entity;

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
@SQLDelete(sql = "UPDATE place_group_blocks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PlaceGroupBlock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "place_block_id", nullable = false)
	private Long placeBlockId;

	@Column(name = "place_group_id", nullable = false)
	private Long placeGroupId;
}
