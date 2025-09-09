package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 상세 조회 응답")
public record ProjectGetResponse(
    @Schema(description = "조회된 프로젝트 정보")
    ProjectDto project
) {
    public static ProjectGetResponse from(ProjectDto projectDto) {
        return new ProjectGetResponse(projectDto);
    }
}
