package com.green.green.controller;

import com.green.green.dto.ApiResponse;
import com.green.green.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@AllArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> like(@PathVariable int id) {
        likeService.like(id);
        return ResponseEntity.ok().body(ApiResponse.ok());
    }
}
