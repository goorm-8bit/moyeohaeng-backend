package eightbit.moyeohaeng.domain.project.common.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.stream.IntStream;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectId;
import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.event.message.MessagePublisher;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ProjectEventAspect {

	private final ChannelTopic projectChannel;
	private final MessagePublisher messagePublisher;

	@AfterReturning(pointcut = "@annotation(projectEvent)", returning = "result")
	public void publishProjectEvent(JoinPoint joinPoint, ProjectEvent projectEvent, Object result) {
		Long projectId = findProjectId(joinPoint);
		MessageBody messageBody = MessageBody.of(
			projectId,
			projectEvent.eventType().name(),
			Map.of(
				"actionType", projectEvent.actionType().name(),
				"payload", result
			)
		);

		messagePublisher.convertAndSend(projectChannel.getTopic(), messageBody);
	}

	private Long findProjectId(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		Parameter[] parameters = method.getParameters();
		Object[] args = joinPoint.getArgs();

		return IntStream.range(0, parameters.length)
			.filter(i -> parameters[i].isAnnotationPresent(ProjectId.class) && args[i] instanceof Long)
			.mapToObj(i -> (Long)args[i])
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("파라미터에서 @ProjectId(Long) 값을 찾을 수 없습니다."));
	}
}
