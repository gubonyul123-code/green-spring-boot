package com.green.green.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// 얘는 사용자 요청을 controller 까지 운반만 하고 그 외 하는 거 없음
// 엔티티도, 레포지토리도, 서비스도, 컨트롤러도 아니다.
// 그럼 대체 얘는 뭐냐. 바로 DTO다.
// DTO는 Data Transfer Object (데이터 운반 객체)
// 사용자 데이터를 "운반"할 때만 쓰이는 객체(클래스)라서 DTO라고 불린다..
public class PostUpdateRequest {
    @NotBlank
    @Size(min = 10)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String content;
}
