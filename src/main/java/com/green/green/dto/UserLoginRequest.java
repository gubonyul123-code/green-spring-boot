package com.green.green.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
