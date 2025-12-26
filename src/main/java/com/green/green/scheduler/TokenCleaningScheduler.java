package com.green.green.scheduler;

import com.green.green.repository.AccessTokenBlacklistRepository;
import com.green.green.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public class TokenCleaningScheduler {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    // 매일 새벽 01시에 'RT' 와 'AT블랙리스트' 테이블 내
    // 만료기간이 지난 데이터를 물리 삭제하는 스케줄러
    @Scheduled(cron = "0 0 1 1/1 * ?")    // 매일 새벽 1시에 1번 실행
    @Transactional
    public void clearnTokens(){
        LocalDateTime now = LocalDateTime.now();
        log.warn("토큰 정리 스케줄러가 실행됩니다. 실행시간 : {}", now);

        // 1. RT 테이블에서, 만료시간이 지난 데이터들을 찾아서 모두 지운다.
        refreshTokenRepository.deleteAllByExpirationDatetimeBefore(now);

        // 2. AT블랙리스트 테이블에서, 만료시간이 지난 데이터들을 찾아서 모두 지운다.
        accessTokenBlacklistRepository.deleteAllByExpirationDatetimeBefore(now);

        log.warn("토큰 정리 스케줄러 적업이 성공적으로 마무리되었습니다.  실행 시작 시간 : {}", now);
    }
}

