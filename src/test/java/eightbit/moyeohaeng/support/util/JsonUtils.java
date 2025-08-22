package eightbit.moyeohaeng.support.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 객체를 JSON 문자열로 변환합니다.
	 * @param object 변환할 객체
	 * @return JSON 문자열
	 */
	public String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON 직렬화에 실패했습니다.", e);
		}
	}

	/**
	 * MvcResult의 응답 본문을 원하는 클래스의 객체로 변환합니다.
	 * @param mvcResult MockMvc 실행 결과
	 * @param clazz 변환할 클래스 타입
	 * @return 변환된 객체
	 */
	public <T> T fromJson(MvcResult mvcResult, Class<T> clazz) {
		try {
			String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
			return objectMapper.readValue(responseBody, clazz);
		} catch (IOException e) {
			throw new RuntimeException("JSON 역직렬화에 실패했습니다.", e);
		}
	}
}
