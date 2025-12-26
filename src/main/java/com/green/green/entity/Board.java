package com.green.green.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// JPA가 DB에서 가져온 데이터를 최초로 넣는 곳 (Entity)
@Entity // DB 테이블에서 스프링부트로 데이터를 갖고 왔을 때 담는 상자(JPA에게 DB에서 어디로 가는지 알려주는 역할)
// JPA가 어느 테이블로 찾아가야 하는지 알려주는 곳
@Table(name = "boards") // <- mysql 테이블명
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Board {
    // DB에서 id라는 필드는 기본키라는 것을 알림
    @Id
    // DB에서 id라는 필드는auto_increment 처리되어 있음을 알림
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 auto_increment 되어 있는 값이니까 바꾸니마(JPA한테 알려줌)
    private int id;

    // 테이블의 title 컬럼에서 가져온 값은 여기 (title 필드)에 세팅하라고 알림
    @Column(name = "title", nullable = false)   // DB와 JPA 컬럼 매칭(nullable = false 의미 : not null)
    private String title;

    // 테이블의 content 컬럼에서 가져온 값은 여기에 세팅하라고 알림
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "hits", nullable = false)
    private int hits;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @CreatedDate
    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}



