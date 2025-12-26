package com.green.green.global;

import com.green.green.entity.AccessTokenBlacklist;
import com.green.green.repository.AccessTokenBlacklistRepository;
import jakarta.servlet.FilterChain;  // 다음 필터로 넘기는 체인
import jakarta.servlet.ServletException;    // 예외 처리용
import jakarta.servlet.http.HttpServletRequest;  // 클라이언트가 보낸 요청 정보 (헤더, 바디 등)
import jakarta.servlet.http.HttpServletResponse;  // 서버가 보낼 응답 정보
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  // 인증 정보를 담는 상자
import org.springframework.security.core.context.SecurityContextHolder;  // 인증 정보 저장소
import org.springframework.security.core.userdetails.UserDetails;  // Spring Security 표준 사용자 정보
import org.springframework.security.core.userdetails.UserDetailsService;  // DB에서 사용자 정보 가져오는 서비스
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;  // 요청당 딱 1번만 실행되는 필터의 부모 클래스
import java.io.IOException;  // 예외 처리용
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter : 요청당 무조건 1번만 실행(중복 실행 방지)
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;

    @Override  // 메서드 재정의
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization"); // Authorization이라는 헤더의 정보를 빼오기

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // Bearer (7글자) 빼고 실질적은 토큰값만 가져오겠다
            // 토큰 위/변조 여부 검사
            if (tokenProvider.validateToken(token)) {
                // 블랙리스트 여부 확인
                Optional<AccessTokenBlacklist> optionalATB = accessTokenBlacklistRepository.findByToken(token);
                if (optionalATB.isEmpty()) {
                    String username = tokenProvider.getUsernameFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);  //  Spring Security가  UserDetails만 이해함( User 엔티티 이해x)

                    // Spring Security 인증 설정
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,    // 주체 (사용자 정보)
                                    null,           // 자격 증명 (비밀번호), 이미 토큰으로 인증해서 jwt 방식에서는 불필요
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}