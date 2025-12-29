package com.green.green.controller;

import com.green.green.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class TestController {
    private final EmailService mailService;

    @GetMapping("hello")
    public String test() {
        return "hello spring!";
    }
}
