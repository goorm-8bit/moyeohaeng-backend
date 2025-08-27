package eightbit.moyeohaeng.global.handler.test;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 핸들러 테스트를 위한 더미 컨트롤러
 */
@RestController
@RequestMapping("/test/handler")
public class HandlerTestController {

    /**
     * 단순 문자열 반환 (SuccessResponseHandler 테스트용)
     */
    @GetMapping("/string")
    public String getStringResponse() {
        return "test-string-response";
    }
    
    /**
     * 객체 반환 (SuccessResponseHandler 테스트용)
     */
    @GetMapping("/object")
    public TestDto getObjectResponse() {
        return new TestDto("test-object-data");
    }
    
    /**
     * ResponseEntity 반환 (SuccessResponseHandler 테스트용)
     */
    @GetMapping("/entity")
    public ResponseEntity<TestDto> getEntityResponse() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TestDto("test-entity-data"));
    }
    
    /**
     * 명시적 SuccessResponse 반환
     */
    @GetMapping("/success-response")
    public SuccessResponse<TestDto> getSuccessResponse() {
        return SuccessResponse.of(eightbit.moyeohaeng.global.success.CommonSuccessCode.SELECT_SUCCESS, new TestDto("test-success-response-data"));
    }
    
    /**
     * RuntimeException 발생 (ExceptionHandler 테스트용)
     */
    @GetMapping("/runtime-exception")
    public void throwRuntimeException() {
        throw new RuntimeException("테스트용 RuntimeException");
    }
    
    /**
     * BaseException 발생 (ExceptionHandler 테스트용)
     */
    @GetMapping("/base-exception")
    public void throwBaseException() {
        throw new TestException();
    }
    
    /**
     * IllegalArgumentException 발생 (ExceptionHandler 테스트용)
     */
    @GetMapping("/illegal-argument")
    public void throwIllegalArgumentException() {
        throw new IllegalArgumentException("테스트용 IllegalArgumentException");
    }
    
    /**
     * 헤더 필수 엔드포인트 (MissingRequestHeaderException 테스트용)
     */
    @GetMapping("/required-header")
    public TestDto getWithRequiredHeader(@RequestHeader("X-Required-Header") String header) {
        return new TestDto(header);
    }
    
    /**
     * @Valid 검증 실패 테스트 (MethodArgumentNotValidException 테스트용)
     */
    @PostMapping("/validation")
    public TestDto validateRequest(@RequestBody @Valid TestRequestDto request) {
        return new TestDto(request.getValue());
    }
    
    /**
     * 테스트용 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDto {
        private String value;
    }
    
    /**
     * 테스트용 Request DTO (검증 포함)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestRequestDto {
        @NotBlank(message = "값은 필수입니다")
        private String value;
    }
    
    /**
     * 테스트용 커스텀 예외
     */
    public static class TestException extends BaseException {
        public TestException() {
            super(TestErrorCode.TEST_ERROR);
        }
    }
    
    /**
     * 테스트용 에러 코드
     */
    public enum TestErrorCode implements ErrorCode {
        TEST_ERROR(HttpStatus.BAD_REQUEST, "TEST001", "테스트 에러가 발생했습니다");
        
        private final HttpStatus status;
        private final String code;
        private final String message;
        
        TestErrorCode(HttpStatus status, String code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
        
        @Override
        public String getCode() {
            return code;
        }
        
        @Override
        public String getMessage() {
            return message;
        }
        
        @Override
        public HttpStatus getStatus() {
            return status;
        }
    }
}
