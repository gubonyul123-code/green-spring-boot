package com.green.green.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_code")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "expiration_datetime", nullable = false)
    private LocalDateTime expirationDatetime;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;
}
