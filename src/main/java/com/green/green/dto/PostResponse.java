package com.green.green.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
