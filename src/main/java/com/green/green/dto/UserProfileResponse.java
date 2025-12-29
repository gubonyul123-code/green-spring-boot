package com.green.green.dto;

import com.green.green.utils.DatetimeFunction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProfileResponse {
    private String name;
    private String createdDatetime;

    public UserProfileResponse(String name, LocalDateTime createdDatetime) {
        this.name = name;

        DatetimeFunction datetimeFunction = new DatetimeFunction();
        this.createdDatetime = datetimeFunction.getElapsedTime(createdDatetime);
    }
}
