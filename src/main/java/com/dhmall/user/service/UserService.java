package com.dhmall.user.service;

import com.dhmall.exception.ErrorCode;
import com.dhmall.exception.UserAccountException;
import com.dhmall.user.dto.LoginDto;
import com.dhmall.user.dto.MailDto;
import com.dhmall.user.dto.UserDto;
import com.dhmall.user.mapper.UserMapper;
import com.dhmall.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final EmailService emailService;
    private LoginDto loginUser;

    @Transactional
    public UserDto registerUser(UserDto newUser) {

        // 사용자 비밀번호 암호화
        newUser.setPassword(SecurityUtil.encryptInfo(newUser.getPassword()));

        // 가입 인증 메일 전송(비동기)
        MailDto email = new MailDto();
        email.setName(newUser.getName());
        email.setEmail(newUser.getEmail());
        emailService.sendEmail(email);

        newUser.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        newUser.setUpdatedAt(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

        this.userMapper.insertUser(newUser);

        return newUser;
    }

    @Transactional
    public void verifyEmail(String email) {
        this.userMapper.updateAuthStatus(email);
    }

    public String checkDuplicateId(String nickname) {
        UserDto user = userMapper.findById(nickname);
        if(user != null) throw new UserAccountException(HttpStatus.CONFLICT, ErrorCode.CLIENT_ALREADY_EXISTED_ACCOUNT_ERROR, "이미 등록된 아이디입니다.");
        return nickname;
    }

    public void login(String nickname, String password) {
        loginUser = new LoginDto();
        UserDto userFromDB = this.userMapper.findById(nickname);
        boolean isPasswdMatched = SecurityUtil.verifyEncryption(password, userFromDB.getPassword());

        // 회원가입 여부
        if(userFromDB.getNickname().equals("")) {
            throw new UserAccountException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_NOT_REGISTERED_ACCOUNT_ERROR, "등록되지 않은 계정입니다.");
        }

        // 이메일 인증 여부
        if(userFromDB.getAuthStatus() == 0) {
            throw new UserAccountException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_UNVERIFIED_EMAIL_ACCOUNT_ERROR, "이메일 인증이 필요합니다. 이메일 인증 여부를 확인해주세요.");
        }

        if(!userFromDB.getNickname().equals(nickname)) {
            throw new UserAccountException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_ID_PASSWORD_MISMATCH_ERROR, "입력하신 아이디를 확인해주세요.");
        }

        if(!isPasswdMatched) {
            throw new UserAccountException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_ID_PASSWORD_MISMATCH_ERROR, "입력하신 비밀번호를 확인해주세요.");
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
