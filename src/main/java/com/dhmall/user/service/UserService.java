package com.dhmall.user.service;

import com.dhmall.exception.UserException;
import com.dhmall.user.dto.LoginDto;
import com.dhmall.user.dto.UserDto;
import com.dhmall.user.jwt.TokenProvider;
import com.dhmall.user.mapper.UserMapper;
import com.dhmall.util.UserUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class.getName());

    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;
    private LoginDto loginUser;

    @Value("${spring.mail.username}")
    private String from;

    private JavaMailSender mailSender;

    public UserService(UserMapper userMapper, JavaMailSender mailSender, TokenProvider tokenProvider) {
        this.userMapper = userMapper;
        this.mailSender = mailSender;
        this.tokenProvider = tokenProvider;
    }

    @Transactional(rollbackFor = {java.lang.Exception.class})
    public void registerUser(UserDto newUser) {
        // authKey 생성
        newUser.setAuthStatus(0);
        newUser.setAuthKey(UserUtil.encryptInfo(newUser.getUserId(), "").toString());

        // 사용자 비밀번호 암호화
        newUser.setPassword(UserUtil.encryptInfo(newUser.getPassword(), newUser.getAuthKey()).toString());

        // 가입 인증 메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(newUser.getEmail());
        message.setSubject("Sago 서비스 가입인증 요청");
        message.setSentDate(Date.from(Instant.now()));
        message.setText("회원가입 완료를 위해 이곳을 눌러주세요.");
        message.setText(new StringBuffer().append("<h1>Sago 서비스 가입 메일인증 입니다</h1>")
                .append("<a href='http://localhost:8080/users/verifyEmail?email=")
                .append(newUser.getEmail()).append("&key=").append(newUser.getAuthKey())
                .append("' target='_blenk'>가입 완료를 위해 이메일 이곳을 눌러주세요</a>").toString());

        newUser.setCreatedAt(Timestamp.from(Instant.now()));
        newUser.setUpdatedAt(Timestamp.from(Instant.now()));

        this.userMapper.insertUser(newUser);

        mailSender.send(message);
    }

    @Transactional(rollbackFor = {java.lang.NullPointerException.class})
    public void verifyEmail(String email) {
        this.userMapper.updateAuthStatus(email);
    }

    @Transactional
    public String checkDuplicateId(String userId) {
        UserDto user = userMapper.findById(userId);
        if(user != null) throw new UserException("이미 등록된 아이디입니다.");
        return userId;
    }

    @Transactional
    public void login(String userId, String password) {

        String authKey = UserUtil.encryptInfo(userId, "").toString();
        String encrypedPasswd = UserUtil.encryptInfo(password, authKey).toString();
        UserDto userFromDB = this.userMapper.findByIdAndPassword(userId, encrypedPasswd);

        // 회원가입 여부
        if(userFromDB == null) {
            throw new UserException("등록되지 않은 계정입니다.");
        }

        // 이메일 인증 여부
        if(userFromDB.getAuthStatus() == 0) {
            throw new UserException("이메일 인증이 필요합니다. 이메일 인증 여부를 확인해주세요.");
        }

        loginUser = LoginDto.builder().userId(userId).password(password).build();
    }

    public LoginDto getLoginUser() {
        return loginUser;
    }

    @Transactional
    public UserDto info(String userId) {
        return userMapper.findById(userId);
    }

}
