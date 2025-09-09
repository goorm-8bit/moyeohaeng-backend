package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "프로젝트 검색 응답")
public record ProjectSearchResponse(
    @Schema(description = "검색된 프로젝트 목록")
    List<ProjectDto> projects
) {
    public static ProjectSearchResponse from(List<ProjectDto> projects) {
        return new ProjectSearchResponse(projects);
    }
}
