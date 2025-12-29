package com.green.green.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder    // 빌더 패턴 사용 가능하게 함
public class ApiResponse<T> {   // T는 제네릭? : '자바 제네릭' 문법의 일부 (T = ?) / 어떤 타입이든 가능
    // 필드: 성공 여부, 메시지, 데이터
    private boolean success;
    private String message;
    private T data;

    // 성공 응답 생성 메서드 (데이터 있음)
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    // 성공 응답 생성 메서드 (데이터 없음)
    public static <T> ApiResponse<T> ok() {
        return ApiResponse.<T>builder()
                .success(true)
                .build();
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
