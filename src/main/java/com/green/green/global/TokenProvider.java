package com.green.green.global;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Component // 스프링이 이 클래스의 객체를 자동으로 만들어줌, 다른 곳에서 @Autowired로 주입받아 사용 가능
public class TokenProvider {
    private final Key key; // JWT에 서명할 때 사용하는 '비밀 키' (이 키가 있어야 토큰을 만들고 검증 가능)

    // 엑세스 토큰의 지속 시간 (초)
    private static final long ACCESS_TOKEN_VAILDITY = 1000 * 60 * 60;    // 1시간 (기본 단위 '밀리초',  1초 = 1000밀리초)

    // 리프레시 토큰의 지속시간(초)
    private static final long REFRESH_TOKEN_VAILDITY = 1000 * 60 * 60 * 24;  // 24시간

    public TokenProvider(@Value("${spring.jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 엑세스 토큰 생성
    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VAILDITY);

        return Jwts.builder()  // JWT 빌더 시작 (JWT를 만들 준비 시작, 체인처럼 메서드 연결)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)    // (서명, 암호화방식), 위변조 방지
                .compact();  // JWT 토큰 문자열 생성
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_VAILDITY);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성(위/변조 여부) 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()    // 토큰을 해석할 파서(Parser) 만들기 시작
                    .setSigningKey(key)
                    .build()    // 파서 생성 완료
                    .parseClaimsJws(token);  // 토큰 파싱 시도 (검증 + 내용 읽기)
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    // jwt에서 username을 다시 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()   //  JWT의 Payload 부분 가져오기
                .getSubject();  // "subject" 필드 값 가져오기, 토큰 만들 때 username 넣음
    }

    public Date getExpiration(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();   // 만료 시간 가져오기
    }
}