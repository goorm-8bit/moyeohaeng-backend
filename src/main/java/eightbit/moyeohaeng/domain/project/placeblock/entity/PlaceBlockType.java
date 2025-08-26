package eightbit.moyeohaeng.domain.project.placeblock.entity;

/**
 * 장소 블록의 유형을 정의합니다.
 * 클라이언트/서버 간 직렬화 및 검증에 사용됩니다.
 *
 * 스레드 안전성: enum 특성상 불변이며 스레드 세이프합니다.
 * 시간/타임존 의존성: 없음.
 */
public enum PlaceBlockType {
	PIN,
	PLACE_BLOCK
}