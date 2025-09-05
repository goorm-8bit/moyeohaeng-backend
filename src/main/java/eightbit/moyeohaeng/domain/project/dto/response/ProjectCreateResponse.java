package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 생성 응답")
public record ProjectCreateResponse(
    @Schema(description = "생성된 프로젝트 정보")
    ProjectDto project
) {
    public static ProjectCreateResponse from(ProjectDto projectDto) {
        return new ProjectCreateResponse(projectDto);
    }
}
