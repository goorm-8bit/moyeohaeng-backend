package eightbit.moyeohaeng.domain.project.dto.condition;

import eightbit.moyeohaeng.domain.project.dto.request.ProjectSortType;
import lombok.Builder;

@Builder
public record ProjectSearchCondition(
	Long teamId,
	ProjectSortType sortType
) {
	public ProjectSearchCondition {
		if (sortType == null) {
			sortType = ProjectSortType.MODIFIED_AT_DESC;
		}
	}

	public boolean hasTeamFilter() {
		return teamId != null;
	}

}
