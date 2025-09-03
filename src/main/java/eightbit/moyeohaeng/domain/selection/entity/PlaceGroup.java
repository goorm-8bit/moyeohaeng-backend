package eightbit.moyeohaeng.domain.selection.entity;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "place_groups")
@SQLDelete(sql = "UPDATE place_groups SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class PlaceGroup extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "color", length = 7)
	private String color;

	public static PlaceGroup of(String name, String color) {
		return builder()
			.name(name)
			.color(color)
			.build();
	}
}
