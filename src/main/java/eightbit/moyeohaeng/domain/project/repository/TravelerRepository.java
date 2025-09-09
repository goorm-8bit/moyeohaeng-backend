package eightbit.moyeohaeng.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eightbit.moyeohaeng.domain.project.entity.Traveler;

public interface TravelerRepository extends JpaRepository<Traveler, Long> {
    boolean existsByProject_IdAndMember_Id(Long projectId, Long memberId);
}
