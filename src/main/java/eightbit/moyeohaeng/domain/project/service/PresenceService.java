package eightbit.moyeohaeng.domain.project.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectId;
import eightbit.moyeohaeng.domain.project.dto.response.PresenceDeleteResponse;
import eightbit.moyeohaeng.domain.project.dto.response.PresenceResponse;
import eightbit.moyeohaeng.domain.project.repository.PresenceRepository;
import eightbit.moyeohaeng.global.dto.UserInfo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresenceService {

	private final PresenceRepository presenceRepository;

	@ProjectEvent(eventType = EventType.PRESENCE, actionType = ActionType.CREATED)
	public PresenceResponse subscribe(@ProjectId Long projectId, UUID uuid, UserInfo userInfo) {
		presenceRepository.savePresence(projectId, uuid, userInfo);
		return PresenceResponse.of(userInfo);
	}

	@ProjectEvent(eventType = EventType.PRESENCE, actionType = ActionType.DELETED)
	public PresenceDeleteResponse unsubscribe(@ProjectId Long projectId, UUID uuid) {
		presenceRepository.deletePresence(projectId, uuid);
		return PresenceDeleteResponse.of(uuid);
	}

	public List<PresenceResponse> getConnectedMembers(Long projectId) {
		return presenceRepository.findAllPresence(projectId).stream()
			.map(PresenceResponse::of)
			.toList();
	}
}
