package eightbit.moyeohaeng.domain.auth;

/**
 * Domain-agnostic access roles usable across Project, Team, etc.
 */
public enum UserRole {
	OWNER,
	MEMBER,
	TRAVELER,
	GUEST,
	VIEWER,
	ANY
}
