package com.green.green.service;

import com.green.green.dto.UserProfileResponse;
import com.green.green.dto.UserResponse;
import com.green.green.dto.UserUpdateRequest;
import com.green.green.entity.User;
import com.green.green.exceptions.AuthenticationFailureException;
import com.green.green.exceptions.ResourceNotFoundException;
import com.green.green.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getOtherUsersDetail(int id) {
        // 사용자가 준 타인 ID로 유저를 조회해서, 닉네임, 가입일 내릴것이다.
        Optional<User> targetUserOptional = userRepository.findById(id);    // Optional<T>는 null이 올 수 있는 값을 감싸는 Wrapper 클래스, Optional은 반환타입으로만 사용
        if(targetUserOptional.isEmpty()) {
            throw new ResourceNotFoundException("유저를 찾을 수 없습니다.");
        }

        User targetUser = targetUserOptional.get();
        if(targetUser.isDeleted()) {
            throw new ResourceNotFoundException("탈퇴한 유저입니다.");
        }

        UserResponse ur = new UserResponse(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getCreatedDatetime()
        );

        return ur;
    }


    public UserProfileResponse getOtherUsersProfile(int id) {
        Optional<User> targetUserOptional = userRepository.findById(id);

        if(targetUserOptional.isEmpty()) {
            throw new ResourceNotFoundException("유저를 찾을 수 없습니다.");
        }

        User targetUser = targetUserOptional.get();
        if(targetUser.isDeleted()) {
            throw new ResourceNotFoundException("탈퇴한 유저입니다.");
        }

        UserProfileResponse upr = new UserProfileResponse(
                targetUser.getName(),
                targetUser.getCreatedDatetime()
        );

        return upr;
    }

    public User getMyDetail() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return user;
    }

    // 내 정보 수정
    public void updateUserInfo(UserUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        if(user.getUsername() != null){
            user.setName(request.getUsername());
            userRepository.save(user);
        }
    }

    public void deleteUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        user.setDeleted(true);
        userRepository.save(user);
    }
}
