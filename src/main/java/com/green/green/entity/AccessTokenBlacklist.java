package com.green.green.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="access_token_blacklist")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccessTokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_datetime", nullable = false)
    private LocalDateTime expirationDatetime;
}
