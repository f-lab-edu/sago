package com.dhmall.user.controller;

import com.dhmall.exception.UserException;
import com.dhmall.user.jwt.JwtFilter;
import com.dhmall.user.dto.LoginDto;
import com.dhmall.user.dto.UserDto;
import com.dhmall.user.jwt.TokenProvider;
import com.dhmall.user.service.LoginSecurityService;
import com.dhmall.user.service.UserService;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/users/")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class.getName());

    private final UserService userService;
    private final LoginSecurityService loginSecurityService;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public UserController(UserService userService, LoginSecurityService loginSecurityService,
                          TokenProvider tokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.loginSecurityService = loginSecurityService;
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody @NonNull LoginDto user) {
        userService.login(user.getUserId(), user.getPassword());
        String accessToken = tokenProvider.createToken(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        return new ResponseEntity<>(accessToken, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("signUp")
    @ResponseStatus(value=HttpStatus.ACCEPTED)
    public String signUp(@RequestBody @NonNull UserDto newUser) {
        Optional.ofNullable(newUser)
                .map(user -> newUser.getUserId())
                .map(user -> newUser.getAddress())
                .map(user -> newUser.getName())
                .map(user -> newUser.getEmail())
                .map(user -> newUser.getPassword())
                .map(user -> newUser.getBirth())
                .map(user -> newUser.getPhoneNumber())
                .orElseThrow(() -> new NullPointerException("회원가입 시 필요한 정보를 모두 입력해주세요."));

        userService.registerUser(newUser);
        return "회원 등록이 완료되었습니다.";
    }

    @PostMapping("verifyEmail")
    @ResponseStatus(value=HttpStatus.OK)
    public String verify(@RequestBody @NonNull String email) {
        userService.verifyEmail(email);
        return "회원 인증이 완료되었습니다.";
    }

    @PostMapping("isAlreadyUsed")
    @ResponseStatus(value=HttpStatus.OK)
    public String checkDuplicateId(@RequestBody @NonNull String userId) {
        userService.checkDuplicateId(userId);
        return "사용 가능한 ID입니다.";
    }

    @GetMapping("logout")
    @ResponseStatus(value=HttpStatus.OK)
    public String logout() {
        redisTemplate.delete(loginSecurityService.getCurrentLoginUsername().trim());
        return "정상적으로 로그아웃 되었습니다.";
    }

    @GetMapping("info")
    @ResponseStatus(value= HttpStatus.OK)
    public UserDto info(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtFilter.AUTHORIZATION_HEADER).substring(7);
        if(!redisTemplate.opsForValue().getOperations().hasKey(loginSecurityService.getCurrentLoginUsername().trim())) throw new UserException("먼저 로그인 해주세요.");
        if(!tokenProvider.validateToken(accessToken)) {
            String newAccessToken = tokenProvider.refreshToken(loginSecurityService.getCurrentLoginUsername());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + newAccessToken);
        }

        UserDto userInfo = userService.info(loginSecurityService.getCurrentLoginUsername());
        return userInfo;
    }
}
