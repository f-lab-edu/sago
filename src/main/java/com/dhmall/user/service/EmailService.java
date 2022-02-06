package com.dhmall.user.service;

import com.dhmall.user.dto.MailDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freeMarkerConfig;

    @Value("${spring.mail.username}")
    private final String from;

    @SneakyThrows
    @Async
    public void sendEmail(MailDto email) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "utf-8");

        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(email.getEmail());
        mimeMessageHelper.setSubject("Sago 서비스 가입인증 요청");
        mimeMessageHelper.setSentDate(Date.from(Instant.now()));

        Template t = freeMarkerConfig.getTemplate("email-template.ftl");

        Map model = new HashMap();
        model.put("name", email.getName());
        model.put("email", email.getEmail());

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        mimeMessageHelper.setText(html, true);

        mailSender.send(message);
    }
}
