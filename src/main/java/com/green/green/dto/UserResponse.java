package com.green.green.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성, @NoArgsConstructor: 기본 생성자 생성(=빈 생성자)
public class UserResponse {
    private int id;
    private String username;
    private LocalDateTime createdDatetime;
}
