package eightbit.moyeohaeng.domain.project.common.aop;

import org.springframework.stereotype.Service;

import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectId;

@Service
public class TestProjectEventAspectService {

	public record TestResponse(Long projectId, String data, EventType eventType, ActionType actionType) {
	}

	@ProjectEvent(eventType = EventType.PROJECT, actionType = ActionType.CREATED)
	public TestResponse handleProjectCreate(@ProjectId Long projectId, String data) {
		return new TestResponse(projectId, data, EventType.PLACE_BLOCK, ActionType.CREATED);
	}

	@ProjectEvent(eventType = EventType.PROJECT, actionType = ActionType.UPDATED)
	public TestResponse handleProjectUpdate(@ProjectId Long projectId, String data) {
		return new TestResponse(projectId, data, EventType.PLACE_BLOCK, ActionType.UPDATED);
	}

	@ProjectEvent(eventType = EventType.PROJECT, actionType = ActionType.DELETED)
	public TestResponse handleProjectDelete(@ProjectId Long projectId, String data) {
		return new TestResponse(projectId, data, EventType.PLACE_BLOCK, ActionType.DELETED);
	}
}
