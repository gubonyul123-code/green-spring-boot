package com.green.green.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
}
