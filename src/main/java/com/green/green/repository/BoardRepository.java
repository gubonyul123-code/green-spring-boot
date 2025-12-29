package com.green.green.repository;

import com.green.green.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {    // JpaRepository 상속 받음. JpaRepository은 db와 통신을 위한 완성되어 있는 코드
    List<Board> findBoardsByIsDeletedFalse(Pageable pageable);
    List<Board> findBoardsByAuthorId(int id);

    // MATCH(title) : title 컬럼에서, AGAINST(:keyword -  이 부분이 메서드 매개변수와 연결됨) : keyword와 매칭되는 행 찾기
    @Query(value = "SELECT * FROM boards WHERE MATCH(title) AGAINST(:keyword)", nativeQuery = true) // nativeQuery : 실제 테이블명 등을 직접 사용한다는 선언
    List<Board> searchByTitle(@Param("keyword") String keyword);
}
