package com.example.backend.global.common.exception;

import com.example.backend.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 커스텀 비즈니스 예외 처리 (CustomException)
     * 예: TRANSACTION_NOT_FOUND, DUPLICATE_EMAIL, FORBIDDEN 등
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    /**
     * 2. DTO 유효성 검증 실패 (@Valid) -> 400 VALIDATION_ERROR
     * 컨트롤러 입구에서 @Valid 조건(금액 <= 0, 날짜 누락 등)에 걸렸을 때
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 첫 번째 검증 실패 필드의 디폴트 에러 메시지를 가져옴 (예: "금액은 0보다 커야 합니다.")
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage));
    }

    /**
     * 3. 요청 파라미터 바인딩/타입 불일치 -> 400 VALIDATION_ERROR
     * 예: yearMonth에 "2026-13"이나 문자열 "abc" 입력 시
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("파라미터 '%s'의 값이 올바르지 않은 형식입니다.", e.getName());

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage));
    }

    /**
     * 4. 지원하지 않는 HTTP 메서드 호출 시 -> 400 VALIDATION_ERROR
     * 예: POST 엔드포인트에 GET 요청을 보낸 경우
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.getCode(), "지원하지 않는 HTTP 메서드 요청입니다."));
    }

    /**
     * 5. 기타 잡히지 않은 최상위 예외 처리 -> 500 INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        // 👇 이 줄을 추가하여 예외의 진짜 원인을 콘솔에 출력합니다!
        log.error("Internal Server Error occurred: ", e);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}