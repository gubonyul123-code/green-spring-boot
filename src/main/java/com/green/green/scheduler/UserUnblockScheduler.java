package com.green.green.scheduler;

import com.green.green.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public class UserUnblockScheduler {
    private final UserRepository userRepository;

    // 매일 자정 00시에 언블락 만료기간 지난 데이터 업데이트 스케줄러
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Transactional
    public void unblockExpiredUsers() {
        LocalDateTime now = LocalDateTime.now();
        log.warn("유저 계정 일시정지 해제 스케줄러가 실행됩니다. 실행시간 : {}", now);

        // user 테이블에서 만료시간이 지난 데이터들을 찾아서 만료시간을 지운다.
        // status를 active로 전환한다.
        userRepository.unblockDatetime(now);

        log.warn("일시정지된 유저가 정상적으로 해제되었습니다. 실행시간 : {}", now);
    }
}
