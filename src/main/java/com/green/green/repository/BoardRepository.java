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

    @Query(value = "SELECT * FROM boards WHERE MATCH(title) AGAINST(:keyword)", nativeQuery = true)
    List<Board> searchByTitle(@Param("keyword") String keyword);
}
