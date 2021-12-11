package com.dhmall.user.service;

import com.dhmall.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(UserDto newUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(newUser.getEmail());
        message.setSubject("Sago 서비스 가입인증 요청");
        message.setSentDate(Date.from(Instant.now()));
        message.setText(new StringBuffer().append("<h1>Sago 서비스 가입 메일인증 입니다</h1>")
                .append("<a href='http://localhost:8080/users/verifyEmail?email=")
                .append(newUser.getEmail())
                .append("' target='_blenk'>가입 완료를 위해 이메일 이곳을 눌러주세요</a>").toString());

        mailSender.send(message);
    }
}
