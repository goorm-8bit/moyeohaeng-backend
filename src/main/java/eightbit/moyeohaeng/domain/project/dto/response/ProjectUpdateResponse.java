package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 수정 응답")
public record ProjectUpdateResponse(
    @Schema(description = "수정된 프로젝트 정보")
    ProjectDto project
) {
    public static ProjectUpdateResponse from(ProjectDto projectDto) {
        return new ProjectUpdateResponse(projectDto);
    }
}
