package eightbit.moyeohaeng.domain.auth;

public enum UserRole {
	OWNER(5),
	MEMBER(4),
	TRAVELER(3),
	GUEST(2),
	VIEWER(1),
	ANY(0);

	private final int priority;

	UserRole(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
}
