package eightbit.moyeohaeng.domain.team.exception;

public class TeamNotFoundException extends RuntimeException {
	
	public TeamNotFoundException(Long teamId) {
		super("팀을 찾을 수 없습니다. teamId = " + teamId);
	}
}
