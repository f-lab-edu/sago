package com.dhmall.user.service;

import com.dhmall.exception.ErrorCode;
import com.dhmall.exception.UserAccountException;
import com.dhmall.user.dto.LoginDto;
import com.dhmall.user.dto.UserDto;
import com.dhmall.user.mapper.UserMapper;
import com.dhmall.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final EmailService emailService;
    private LoginDto loginUser;

    @Transactional
    public void registerUser(UserDto newUser) {

        // authKey(SNS API Key) 임시 등록
        newUser.setAuthStatus(0);
        newUser.setAuthKey("SNS API Key Value");

        // 사용자 비밀번호 암호화
        newUser.setPassword(UserUtil.encryptInfo(newUser.getPassword()));

        // 가입 인증 메일 전송(비동기)
        emailService.sendEmail(newUser);

        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        String updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        newUser.setCreatedAt(Timestamp.valueOf(createdAt));
        newUser.setUpdatedAt(Timestamp.valueOf(updatedAt));

        this.userMapper.insertUser(newUser);
    }

    @Transactional
    public void verifyEmail(String email) {
        this.userMapper.updateAuthStatus(email);
    }

    public String checkDuplicateId(String nickname) {
        UserDto user = userMapper.findById(nickname);
        if(user != null) throw new UserAccountException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_ALREADY_EXISTED_ACCOUNT_ERROR, "이미 등록된 아이디입니다.");
        return nickname;
    }

    public void login(String nickname, String password) {
        loginUser = new LoginDto();
        UserDto userFromDB = this.userMapper.findById(nickname);
        log.info(password + " , " + userFromDB.getPassword());
        boolean isPasswdMatched = UserUtil.verifyEncryption(password, userFromDB.getPassword());

        // 회원가입 여부
        if(userFromDB == null) {
            throw new UserAccountException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_NOT_REGISTERED_ACCOUNT_ERROR, "등록되지 않은 계정입니다.");
        }

        if(!isPasswdMatched) {
            throw new UserAccountException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_ID_PASSWORD_MISMATCH_ERROR, "입력하신 아이디 혹은 비밀번호를 확인해주세요.");
        }

        // 이메일 인증 여부
        if(userFromDB.getAuthStatus() == 0) {
            throw new UserAccountException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_UNVERIFIED_EMAIL_ACCOUNT_ERROR, "이메일 인증이 필요합니다. 이메일 인증 여부를 확인해주세요.");
        }

        loginUser.setNickname(nickname);
        loginUser.setPassword(password);
    }

    public LoginDto getLoginUser() {
        return loginUser;
    }

    public UserDto info(String nickname) {
        return userMapper.findById(nickname);
    }

}
