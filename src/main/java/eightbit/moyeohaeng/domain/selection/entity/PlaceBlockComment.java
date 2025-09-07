package eightbit.moyeohaeng.domain.selection.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
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
	name = "place_block_comments",
	indexes = {
		@Index(name = "idx_pbc_place_block_id", columnList = "place_block_id"),
		@Index(name = "idx_pbc_member_id", columnList = "member_id")
	}
)
@SQLDelete(sql = "UPDATE place_block_comments SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class PlaceBlockComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content", nullable = false, length = 500)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_block_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private PlaceBlock placeBlock;

	/**
	 * 댓글 생성 팩토리 메서드
	 *
	 * @param content    댓글 내용
	 * @param member     작성자
	 * @param placeBlock 대상 장소 블록
	 * @return 생성된 댓글 엔티티
	 */
	public static PlaceBlockComment of(String content, Member member, PlaceBlock placeBlock) {
		return PlaceBlockComment.builder()
			.content(content)
			.member(member)
			.placeBlock(placeBlock)
			.build();
	}

	/**
	 * 댓글 내용 수정
	 *
	 * @param newContent 새로운 댓글 내용
	 */
	public void updateContent(String newContent) {
		this.content = newContent;
	}
}
