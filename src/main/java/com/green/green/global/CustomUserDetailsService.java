package com.green.green.global;

import com.green.green.entity.User;
import com.green.green.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails; // Spring Security가 이해하는 사용자 정보 형식
import org.springframework.security.core.userdetails.UserDetailsService; // Spring Security가 제공하는 인터페이스 (약속된 규칙)
import org.springframework.security.core.userdetails.UsernameNotFoundException; // 사용자를 못 찾았을 때 던지는 예외
import org.springframework.stereotype.Service;

@Service // 이 클래스를 Spring의 서비스 컴포넌트로 등록(이 Service클래스를 Bean(부품)으로 등록해서 다른 곳에서 자동으로 주입(DI)할 수 있게 처리하는 것)
@AllArgsConstructor // 모든 필드를 받는 생성자
public class CustomUserDetailsService implements UserDetailsService { // Spring Security의 UserDetailsService 인터페이스를 구현한다는 약속
    private  final UserRepository userRepository;

    @Override   // 원래 있던 메서드를 내 방식으로 재정의
    public UserDetails loadUserByUsername(String username)  // 사용자가 로그인할 때, JWT 필터에서 토큰의 username으로 사용자 정보를 조회할 때 호출
        throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if(user == null) {
        throw  new UsernameNotFoundException(username);
    }

        return org.springframework.security.core.userdetails.User  // Spring Security가 제공하는 User 클래스
                .builder()  // 빌더 패턴 시작 : 하나씩 설정해서 객체를 만듬
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
