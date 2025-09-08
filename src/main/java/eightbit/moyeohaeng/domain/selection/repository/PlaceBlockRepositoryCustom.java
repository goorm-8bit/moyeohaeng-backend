package eightbit.moyeohaeng.domain.selection.repository;

import java.util.List;
import java.util.Map;

import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummary;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummary;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;

public interface PlaceBlockRepositoryCustom {

	List<PlaceBlockResponse> findPlaceBlocks(Long projectId);

	Map<Long, PlaceBlockLikeSummary> findPlaceBlockLikes(List<Long> placeBlockIds, String username);

	Map<Long, PlaceBlockCommentSummary> findPlaceBlockComments(List<Long> placeBlockIds);
}
