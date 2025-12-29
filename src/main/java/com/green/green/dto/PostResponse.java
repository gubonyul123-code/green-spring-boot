package com.green.green.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@AllArgsConstructor
public class PostResponse {
    private int id;
    private String title;
    private String content;
    private String author;
    private long likes;
}
