package com.green.green.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutRequest {
    @NotBlank
    private  String accessToken;
}
