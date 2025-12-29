package com.green.green.repository;

import com.green.green.entity.User;
import com.green.green.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository <VerificationCode, Integer> {
    // 가장 최근에 받은 인증번호 매칭
    Optional<VerificationCode> findByUserIdAndExpirationDatetimeAfterAndIsVerifiedFalse(Integer userId, LocalDateTime expirationDateTime);
}
