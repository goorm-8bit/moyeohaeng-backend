package eightbit.moyeohaeng.domain.itinerary.entity;

import java.time.LocalTime;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.place.entity.Place;
import eightbit.moyeohaeng.domain.project.entity.Project;
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
	name = "time_blocks",
	indexes = @Index(name = "idx_time_blocks_project_id", columnList = "project_id")
)
@Check(constraints = "end_time > start_time")
@SQLDelete(sql = "UPDATE time_blocks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class TimeBlock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "day", nullable = false)
	private Integer day;

	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@Column(name = "memo", length = 50)
	private String memo;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;

	public static TimeBlock of(Integer day, LocalTime startTime, LocalTime endTime, String memo,
		Project project, Place place) {

		return builder()
			.day(day)
			.startTime(startTime)
			.endTime(endTime)
			.memo(memo)
			.project(project)
			.place(place)
			.build();
	}
}
