package eightbit.moyeohaeng.domain.project.common.aop;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.redis.listener.ChannelTopic;

import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.event.message.MessagePublisher;

class ProjectEventAspectTest {

	private final MessagePublisher messagePublisher = mock(MessagePublisher.class);
	private final ChannelTopic projectChannel = new ChannelTopic("test-project-channel");
	private final ProjectEventAspect projectEventAspect = new ProjectEventAspect(projectChannel, messagePublisher);
	private final TestProjectEventAspectService testProjectService;

	ProjectEventAspectTest() {
		TestProjectEventAspectService target = new TestProjectEventAspectService();
		AspectJProxyFactory factory = new AspectJProxyFactory(target);
		factory.addAspect(projectEventAspect);
		factory.setProxyTargetClass(true);
		this.testProjectService = factory.getProxy();
	}

	@Test
	@DisplayName("ProjectEvent 어노테이션이 붙은 메서드 호출시 이벤트가 발행된다")
	void testProjectEventAnnotation() {
		// CREATED 이벤트 테스트
		verifyEventPublishing(
			testProjectService.handleProjectCreate(1L, "data"),
			EventType.PROJECT,
			ActionType.CREATED
		);

		// UPDATED 이벤트 테스트
		verifyEventPublishing(
			testProjectService.handleProjectUpdate(1L, "data"),
			EventType.PROJECT,
			ActionType.UPDATED
		);

		// DELETED 이벤트 테스트
		verifyEventPublishing(
			testProjectService.handleProjectDelete(1L, "data"),
			EventType.PROJECT,
			ActionType.DELETED
		);
	}

	private void verifyEventPublishing(TestProjectEventAspectService.TestResponse response, EventType expectedEventType,
		ActionType expectedActionType) {
		ArgumentCaptor<MessageBody> messageCaptor = ArgumentCaptor.forClass(MessageBody.class);
		verify(messagePublisher).convertAndSend(
			eq(projectChannel.getTopic()),
			messageCaptor.capture()
		);
		reset(messagePublisher);

		MessageBody capturedMessage = messageCaptor.getValue();
		assertThat(capturedMessage.eventId()).isEqualTo(response.projectId());
		assertThat(capturedMessage.eventName()).isEqualTo(expectedEventType.name());

		@SuppressWarnings("unchecked")
		Map<String, Object> payload = (Map<String, Object>)capturedMessage.payload();
		assertThat(payload)
			.containsEntry("actionType", expectedActionType.name())
			.containsKey("payload");
	}
}
