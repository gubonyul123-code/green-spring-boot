package com.green.green.repository;

import com.green.green.entity.Board;
import com.green.green.entity.Like;
import com.green.green.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository <Like, Integer> {

    // 특정 유저가 특정 게시글에 좋아요 눌렀는지 확인
    Optional<Like> findByUserAndBoard(User user, Board board);

    // 좋아요 삭제 (취소)
    void deleteByUserAndBoard(User user, Board board);

    // 게시글의 좋아요 개수 조회
    long countByBoard(Board board);
}