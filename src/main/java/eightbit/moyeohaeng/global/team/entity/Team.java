package eightbit.moyeohaeng.global.team.entity;

import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Team extends BaseEntity {

	@Id
	@Column(name = "team_id")
	private Long id;
	
	
}
