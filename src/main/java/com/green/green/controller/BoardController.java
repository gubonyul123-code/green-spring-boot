package com.green.green.controller;

import com.green.green.dto.ApiResponse;
import com.green.green.dto.PostCreateRequest;
import com.green.green.dto.PostResponse;
import com.green.green.dto.PostUpdateRequest;
import com.green.green.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController  // @Controller(HTTP 요청을 받음) + @ResponseBody(반환하는 데이터를 JSON으로 자동 변환),  REST API는 JSON 데이터를 주고받음
@RequestMapping("/api/board")   // /api/board 관련만 담당하는 직원, RequestMapping : 기본 경로 설정(이 컨트롤러의 모든 메서드는 /api/board로 시작)
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 1. 모든 게시글을, 작성 최신순으로 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllBoards(Pageable pageable) {
        List<PostResponse> results = boardService.getAllBoards(pageable);
        return ResponseEntity.ok(ApiResponse.ok(results));  // 200 OK with 글 데이터들
    }

    // 2. 상세 조회. 글 하나를 클릭했을 때, 그 글의 상세정보 줘야함. (1건 정보)
    // - Get 인 경우 RequestBody 사용하면 안됨 ! 강제는 아니지만 규칙임(RESTful). @RequestBody는 POST/PUT/PATCH에서만 사용
    // - 그럼 데이터를 어떻게 보내야 되냐? URL에 포함시켜서 보낸다. ex)green.tistiory.com/80
    // - /81, /80, /999 바뀔 수 있음. 그래서 URL은 특정 숫자가 아니라 {id}롷 설정
    // - PathVariable 설정까지 해주면, 스프링이 알아서 URL에서 숫자 뽑아서 int id에 갖다줌.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getDetail(@PathVariable int id) {    // <?> = 어떤 타입이든 반환 가능
        PostResponse response = boardService.getDetailPost(id);
        return ResponseEntity.ok(ApiResponse.ok(response));  // ResponseEntity.ok() = 200 OK 상태코드, ResponseEntity는 HTTP 응답을 만드는 객체
    }

    // 3. 새로운 글 작성
    @PostMapping                                // @Valid :  PostCreateRequest에 설정된 유효성 검사 실행
    public ResponseEntity<ApiResponse<Void>> createNewPost(@Valid @RequestBody PostCreateRequest request){  // @RequestBody : HTTP 요청의 Body(본문)에 있는 JSON을 자바 객체로 변환
        int newPostId = boardService.createNewPost(request);
        URI location = URI.create("/getDetail/" + newPostId);  // Location 헤더: 생성된 리소스의 위치 (예: /getDetail/10)
        return ResponseEntity.created(location).body(ApiResponse.ok());    //  201 Created 상태코드 + Location 헤더 반환
    }

    // 4. 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            // 어떤 게시글을 수정하고 싶은지 pathVariable로
            @PathVariable int id,
            // 데이터 수정은 RequestBody로
            @Valid @RequestBody PostUpdateRequest request       // @RequestBody → PostUpdateRequest 객체로 변환
    ){
        boardService.updatePost(id, request);
        return ResponseEntity.ok().body(ApiResponse.ok());
    }

    // 5. 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable int id){
        boardService.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.ok());  // 204 noContent코드를 반환 (요청 성공했지만 반환할 데이터가 없음, 이미 삭제돼서 보여줄 거 없기때문에 삭제는 보통 204 사용)
    }

    // 내가 작성한 글 조회
    @GetMapping("/my-posts")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getMyPosts() {
        return ResponseEntity.ok(ApiResponse.ok(boardService.getMyPosts()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PostResponse>>> searchPosts(@RequestParam String keyword) {
        List<PostResponse> response = boardService.search(keyword);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}