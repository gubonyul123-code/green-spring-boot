package com.green.green.global;

import com.green.green.dto.ApiResponse;
import com.green.green.exceptions.AuthenticationFailureException;
import com.green.green.exceptions.AuthorizationFailureException;
import com.green.green.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Hidden   // 스웨거 한테 이 파일은 Api 아니니까 인식하지 말라고 알려주는 어노테이션
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 코드 전역에서 떨어지는 익셉션을 처리하는 역할
    // ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // AuthorizationFailureException / 권한 실패
    @ExceptionHandler({
            AuthorizationFailureException.class, // 우리 커스텀 권한 거절 익셉션
            AuthorizationDeniedException.class  // 스프링 시큐리티 권한 거절 익셉션
    })
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // AuthenticationFailureException / 로그인 안된 경우
    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthenticated(AuthenticationFailureException e) {
        System.out.println("403 실행");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationError(MethodArgumentNotValidException e){
        String resultMessage = "";
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        for (FieldError error : errors) {
            resultMessage = resultMessage + error.getField() + "은(는)" + error.getDefaultMessage() + "\n";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(resultMessage));
    }

    // Exception(모든 익셉션의 부모 개념 / 그외 처리하지 않은 모든 예외들 처리 / 사전에 만들어둔것들 아니면 부모가 처리)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(e.getMessage()));
    }
}
