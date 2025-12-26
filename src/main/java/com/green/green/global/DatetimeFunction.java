package com.green.green.global;

import java.time.LocalDateTime;
import java.time.ZoneId;


public class DatetimeFunction {

    public String getElapsedTime(LocalDateTime createdTime) {
        LocalDateTime now = LocalDateTime.now();

        long nowMS = now.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();
        long createdMS = createdTime.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();

        long seconds = nowMS - createdMS;

        long days = seconds / 60 / 60 / 24;

        return days + "일 전";
    }
}
