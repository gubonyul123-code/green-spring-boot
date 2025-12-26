package com.green.green.controller;

import com.green.green.dto.ApiResponse;
import com.green.green.dto.UserProfileResponse;
import com.green.green.dto.UserResponse;
import com.green.green.dto.UserUpdateRequest;
import com.green.green.entity.User;
import com.green.green.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 타인 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getOtherUsersDetail(@PathVariable int id) {  // @PathVariable: 경로 변수, 경로 = url에서 가져온 id
        return ResponseEntity.ok(ApiResponse.ok(userService.getOtherUsersDetail(id)));
    }

    // 타인 정보 조회 (가입일자 문자열 형식 변경)
    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getOtherUsersProfile(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getOtherUsersProfile(id)));
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMyDetail() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getMyDetail()));
    }

    // 수정( Patch 맵핑은 '일부' 수정 시 사용, Put 맵핑은 '전체' 수정 시 사용
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(
            @RequestBody UserUpdateRequest request  // @RequestBody : HTTP 요청의 Body(본문)에 있는 JSON 데이터를 자바 객체로 변환
    ) {
        userService.updateUserInfo(request);
        return  ResponseEntity.ok(ApiResponse.ok());
    }

    // 삭제
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
