package com.green.green.service;

import com.green.green.dto.PostCreateRequest;
import com.green.green.dto.PostResponse;
import com.green.green.dto.PostUpdateRequest;
import com.green.green.entity.Board;
import com.green.green.entity.Like;
import com.green.green.entity.User;
import com.green.green.exceptions.AuthenticationFailureException;
import com.green.green.exceptions.AuthorizationFailureException;
import com.green.green.exceptions.ResourceNotFoundException;
import com.green.green.repository.BoardRepository;
import com.green.green.repository.LikeRepository;
import com.green.green.repository.UserRepository;
import com.green.green.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service  // 비즈니스 로직을 처리하는 Service라고 스프링에게 알려줌 (스프링이 자동으로 객체 생성해서 관리함)
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public List<PostResponse> getAllBoards(Pageable pageable){
        List<Board> results = boardRepository.findBoardsByIsDeletedFalse(pageable);    // False : 0(DB)

        List<PostResponse> response = new ArrayList<>();    // 빈 응답 리스트 만들기

        for(Board board : results) {
            User author = board.getAuthor();
            String authorName = author.getName();

            // 좋아요 개수 세기
            long likeCount = likeRepository.countByBoard(board);

            // DTO 객체 만들기
            PostResponse newResult = new PostResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    authorName,
                    likeCount
            );

            response.add(newResult);
        }

        return response;
    }

    public PostResponse getDetailPost(int id) {
        Optional<Board> boardOptional = boardRepository.findById(id);   // Optional = 있을 수도 있고 없을 수도 있는 상자(값이 있으면 꺼내 쓰고, 없으면 예외 처리)

        if(boardOptional.isEmpty()) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }

        Board board = boardOptional.get();

        if(board.isDeleted()) {   // isDeleted : true는 '1'
            throw new ResourceNotFoundException("삭제된 게시글입니다.");
        }

        // 1. board의 author로 user를 조회해서, 작성자의 이름을 얻어내야된다.
        User writer = board.getAuthor();
        String writerName = writer.getName(); // <- 글 작성자의 유저이름 가져왔음.

        long likeCount = likeRepository.countByBoard(board);

        // dto 만들어서, dto를 응답해야된다.
        PostResponse response = new PostResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                writerName,
                likeCount
        );

        // 조회수 늘려주는 로직 : 조회랑은 관련 없음
        board.setHits(board.getHits() + 1); // 현재 조회수 + 1
        boardRepository.save(board); // 변경된 조회수 데이터를 반영해서 DB에 다시 저장

        return response;
    }

    public int createNewPost(PostCreateRequest request) {
        // 지금 로그인한 유저의 정보
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        Board board1 = new Board();  // 빈 게시글 객체 생성
        board1.setTitle(request.getTitle());
        board1.setContent(request.getContent());
        board1.setAuthor(user);

        Board newPost = boardRepository.save(board1);
        return newPost.getId();
    }

    public void updatePost(int id, PostUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        // 옵셔널은 있을 수도 있고, 없을 수도 있다는 의미의 타입이다
        // 이유 : DB에는 게시글 5까지밖에 없는데 사용자가 7 입력하면 JPA가 못찾음
        // -> 그럼 Board board에 담을 게 없어서 null 담음
        // -> 그럼 그 밑에 board.getTitle() 같은 코드가 NullPointerExcepion 나서 폭발함
        // 이런 실수를 방지하기 위해 "없을 수도 있으니 꺼내 쓰기 전에 확인부터해라"를 유도하기 위한 타입
        Optional<Board> boardOptional = boardRepository.findById(id);
        if(boardOptional.isEmpty()) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }

        // 수정할 대상 게시글을 db에서 가져옴
        Board board = boardOptional.get();

        if (board.getAuthor().getId() != user.getId()) {
            throw new AuthorizationFailureException("본인의 게시글만 수정할 수 있습니다.");
        }

        // 게시글 원 작성자의 user id
        int writerId = board.getAuthor().getId();

        // 프론트엔드랑 협의함 : 수정된 값만 json 채워 보내주고, 수정 안 된 값은 null로 채워라
        if(request.getContent() != null){   // null이 아닐 때만 수정
            // DB Update 해야됨
            board.setContent(request.getContent());
        }

        if(request.getTitle() != null){ // null이 아닐 때만 수정
            // DB Update 해야됨
            board.setTitle(request.getTitle());
        }

        boardRepository.save(board);    // 수정했으면 저장
    }

    public void deletePost(int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isEmpty()) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }
        Board board = boardOptional.get();
        int writerId = board.getAuthor().getId();

        if (writerId != user.getId()) {
            throw new AuthorizationFailureException("본인의 게시글만 수정할 수 있습니다.");
        }

        board.setDeleted(true);     // 소프트 삭제
        boardRepository.save(board);
    }

    public List<PostResponse> getMyPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        // List<Board> results = boardRepository.findBoardsByAuthor(3);
        List<Board> results = boardRepository.findBoardsByAuthorId(user.getId());

        // 새로운 결과 전용 상자 제작
        List<PostResponse> response = new ArrayList<>();

        // board를 post response 로 변경하는 로직
        for(Board board : results) {
            long likeCount = likeRepository.countByBoard(board);

            PostResponse newResult = new PostResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    "",
                    likeCount
            );

            response.add(newResult);
        }

        return response;
    }

    public List<PostResponse> search(String keyword) {
        // 검색 -> SQL을 실행
        List<Board> results = boardRepository.searchByTitle(keyword);

        // 결과를 return
        List<PostResponse> response = new ArrayList<>();

        // board를 post response 로 변경하는 로직
        for(Board board : results) {
            long likeCount = likeRepository.countByBoard(board);

            PostResponse newResult = new PostResponse(
                    board.getId(),
                    board.getTitle(),
                    StringUtils.truncate(board.getContent(), 100),  // 컨텐트 100자에 끊기 유틸 사용
                    "",
                    likeCount
            );

            response.add(newResult);
        }

        return response;
    }
}