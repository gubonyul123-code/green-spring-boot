package com.green.green.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private  String acssessToken;
    private  String refreshToken;
}
