package eightbit.moyeohaeng.global.event.message;

public interface MessagePublisher {

	void convertAndSend(String channel, MessageBody body);
}
