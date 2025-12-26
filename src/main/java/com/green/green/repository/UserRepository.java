package com.green.green.repository;

import com.green.green.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // jpa가 함수 이름을 분석함
    // find - 찾는다
    // ByUsername - username 으로 찾는다
    // findByUsername -> where에 username 걸어서 SELECT 하고 싶구나!
    User findByUsername(String username);
}
