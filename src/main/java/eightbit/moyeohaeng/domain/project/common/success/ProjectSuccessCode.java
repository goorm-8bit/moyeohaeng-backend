package eightbit.moyeohaeng.domain.project.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectSuccessCode implements SuccessCode {

	// 200
	CONNECTING_MEMBERS(HttpStatus.OK, "프로젝트에 접속한 멤버를 가져오는데 성공했습니다"),

	// 201
	CREATE_PROJECT(HttpStatus.CREATED, "프로젝트 생성에 성공하였습니다");

	private final HttpStatus status;
	private final String message;
}
