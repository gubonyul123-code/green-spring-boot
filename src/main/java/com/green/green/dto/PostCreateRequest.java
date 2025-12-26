package com.green.green.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateRequest {
    @NotBlank
    @Size(min = 10)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String content;
}
