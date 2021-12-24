package com.dhmall.user.controller;

import com.dhmall.user.jwt.JwtFilter;
import com.dhmall.user.dto.LoginDto;
import com.dhmall.user.dto.UserDto;
import com.dhmall.user.jwt.TokenProvider;
import com.dhmall.user.service.LoginSecurityService;
import com.dhmall.user.service.UserService;
import com.dhmall.util.SagoApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final LoginSecurityService loginSecurityService;
    private final TokenProvider tokenProvider;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto user) {
        userService.login(user.getNickname(), user.getPassword());
        String accessToken = tokenProvider.createToken(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        return new ResponseEntity<>(accessToken, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("signUp")
    public SagoApiResponse<String> signUp(@Valid @RequestBody UserDto newUser) {
        userService.registerUser(newUser);
        return new SagoApiResponse<>(HttpStatus.ACCEPTED, "회원 등록이 완료되었습니다. 서비스 이용을 위해 이메일 인증을 해주세요.");
    }

    @GetMapping("verifyEmail")
    public SagoApiResponse<String> verify(@NonNull String email) {
        userService.verifyEmail(email);
        return new SagoApiResponse<>(HttpStatus.OK, "회원 가입이 완료되었습니다. 로그인하여 서비스를 이용해보세요.");
    }

    @GetMapping("isAlreadyUsed")
    @ExceptionHandler(ValidationException.class)
    public SagoApiResponse<String> checkDuplicateId(@NonNull String nickname) {
        userService.checkDuplicateId(nickname);
        return new SagoApiResponse<>(HttpStatus.OK, "사용 가능한 ID입니다.");
    }

    /**
     * front에서 Header Cookie 삭제 필요
     */
    @GetMapping("logout")
    public ResponseEntity<String> logout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer ");

        return new ResponseEntity<>("", httpHeaders, HttpStatus.OK);
    }

    @GetMapping("info")
    @ResponseStatus(value= HttpStatus.OK)
    public UserDto info(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtFilter.AUTHORIZATION_HEADER).substring(7);
        tokenProvider.validateToken(accessToken);
        UserDto userInfo = userService.info(loginSecurityService.getCurrentLoginUsername());
        return userInfo;
    }
}
