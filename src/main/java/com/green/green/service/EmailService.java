package com.green.green.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;    // 빌드그래들에 추가한 '메일' 코드 가져오기
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gubonyul123@gmail.com");  // 발신자
        message.setTo(toEmail); // 수신자
        message.setSubject("그린보드 서비스에서 발송한 인증번호입니다.");  // 메일 제목
        message.setText("다음 인증번호를 서비스에 입력해주세요 : " + code);  // 메일 내용

        mailSender.send(message);
    }
}
