package com.green.green.repository;

import com.green.green.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository <VerificationCode, Integer> {
}
