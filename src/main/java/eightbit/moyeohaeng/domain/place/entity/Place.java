package eightbit.moyeohaeng.domain.place.entity;

import org.hibernate.annotations.Check;

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
@Table(name = "places")
@Check(constraints = "latitude BETWEEN 33.0 AND 43.0 AND longitude BETWEEN 124.0 AND 132.0")
public class Place extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "latitude", nullable = false, precision = 10)
	private Double latitude;

	@Column(name = "longitude", nullable = false, precision = 10)
	private Double longitude;

	@Column(name = "detail_link")
	private String detailLink;

	@Column(name = "category")
	private String category;

	public static Place of(String name, String address, Double latitude, Double longitude, String detailLink,
		String category) {

		return builder()
			.name(name)
			.address(address)
			.latitude(latitude)
			.longitude(longitude)
			.detailLink(detailLink)
			.category(category)
			.build();
	}
}
