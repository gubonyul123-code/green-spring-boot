package com.green.green.controller;

import com.green.green.dto.ApiResponse;
import com.green.green.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    // 유저 강제탈퇴 (권리자 권한으로)
    @PostMapping("/user/{id}/ban")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> banUser(@PathVariable int id) {
        adminService.banUser(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    // 유저 일시정지 시키기
    @PostMapping("/user/{id}/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> blockUser(@PathVariable int id) {
        adminService.blockUser(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
