package com.green.green.service;

import com.green.green.entity.Board;
import com.green.green.entity.Like;
import com.green.green.entity.User;
import com.green.green.exceptions.ResourceNotFoundException;
import com.green.green.repository.BoardRepository;
import com.green.green.repository.LikeRepository;
import com.green.green.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void like(int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        // 게시글 조회
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isEmpty()) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }
        Board board = boardOptional.get();

        // 이미 좋아요를 눌렀는지 확인
        Optional<Like> likeOptional = likeRepository.findByUserAndBoard(user, board);

        // if문 분기
        if (likeOptional.isPresent()) {
            // 이미 좋아요를 누른 상태 → 좋아요 취소
            likeRepository.deleteByUserAndBoard(user, board);
        } else {
            // 좋아요를 누르지 않은 상태 → 좋아요 추가
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setBoard(board);
            likeRepository.save(newLike);
        }
    }
}
