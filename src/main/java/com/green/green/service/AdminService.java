package com.green.green.service;

import com.green.green.entity.User;
import com.green.green.enums.UserRole;
import com.green.green.enums.UserStatus;
import com.green.green.exceptions.AuthorizationFailureException;
import com.green.green.exceptions.ResourceNotFoundException;
import com.green.green.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class AdminService {
    private final UserRepository userRepository;
    // 유저 일시정지 지속기간 - 7일
    private static final long USER_UNBLOCKED_VAILDITY = 1000 * 60 * 60 * 24 * 7;

    // 유저 강제탈퇴 시키기
    public void banUser(int id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));    // 람다식?

        if(user.getRole() != UserRole.USER) {
            throw new AuthorizationFailureException("관리자는 탈퇴 시킬 수 없습니다.");
        }

        // 엑티브도 아니고, 일시정지도 아니면 일단 탈퇴 상태임(탈퇴인 사람 탈퇴시키기 불가)
        if(user.getStatus() != UserStatus.ACTIVE && user.getStatus() != UserStatus.BLOCKED) {
            throw new ResourceNotFoundException("유저를 찾을 수 없습니다.");
        }

        user.setStatus(UserStatus.BANNED);
        user.setDeleted(true);
        userRepository.save(user);

        log.info("유저 {} 밴 처리 완료", user.getUsername());
    }

    // 유저 일시정지
    public void blockUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("정상 활동중인 유저만 일시정지가 가능합니다.");
        }

        user.setStatus(UserStatus.BLOCKED);
        user.setUnblockDatetime(LocalDateTime.now().plusSeconds(USER_UNBLOCKED_VAILDITY/1000));
        userRepository.save(user);

        log.info("유저 {} 일시정지 처리 완료", user.getUsername());
    }
}
