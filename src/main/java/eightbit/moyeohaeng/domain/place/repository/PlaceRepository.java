package eightbit.moyeohaeng.domain.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.place.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
	Optional<Place> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
