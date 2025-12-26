package com.green.green.entity;

import com.green.green.enums.UserRole;
import com.green.green.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createdDatetime;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)    // enum 변환기
    private UserRole role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "unblock_datetime", nullable = true)
    private LocalDateTime unblockDatetime;
}
