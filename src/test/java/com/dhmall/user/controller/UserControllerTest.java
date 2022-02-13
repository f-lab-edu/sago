package com.dhmall.user.controller;

import com.dhmall.user.dto.LoginDto;
import com.dhmall.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @ClassRule
    private static final GenericContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(3306);

    @Test
    void postLoginURI_thenReturnHttpOk() throws Exception {
        LoginDto validUser = new LoginDto();
        validUser.setNickname("sagotest18");
        validUser.setPassword("test18");
        String content = objectMapper.writeValueAsString(validUser);

        this.mockMvc.perform(post("/users/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void postSignUpURI_thenReturnResponseEntity() throws Exception {
        UserDto validUser = new UserDto();

        String nickname = "pranne1224";
        String email = "melllamodahye@gmail.com";
        String password = "validtest";
        String name = "Dahye Lee";
        String phoneNumber = "010-2321-2123";
        String birth = "1999.12.25";
        String address = "Seoul";

        validUser.setNickname(nickname);
        validUser.setEmail(email);
        validUser.setPassword(password);
        validUser.setName(name);
        validUser.setPhoneNumber(phoneNumber);
        validUser.setBirth(birth);
        validUser.setAddress(address);

        String content = objectMapper.writeValueAsString(validUser);
        this.mockMvc.perform(post("/users/signUp")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseCode", Matchers.is("ACCEPTED")))
                .andExpect(jsonPath("$.data", Matchers.is("회원 등록이 완료되었습니다. 서비스 이용을 위해 이메일 인증을 해주세요.")));
    }

    @Test
    void getVerifyEmailURI_thenReturnResponseEntity() throws Exception {
        this.mockMvc.perform(get("/users/verifyEmail")
                        .param("email", "melllamodahye@gmail.com"))
                .andExpect(jsonPath("$.responseCode", Matchers.is("OK")))
                .andExpect(jsonPath("$.data", Matchers.is("회원 가입이 완료되었습니다. 로그인하여 서비스를 이용해보세요.")));
    }

    @Test
    void getCheckDuplicateIdURI_thenReturnResponseEntity() throws Exception {
        this.mockMvc.perform(get("/users/isAlreadyUsed")
                        .param("nickname", "pranne1225"))
                .andExpect(jsonPath("$.responseCode", Matchers.is("OK")))
                .andExpect(jsonPath("$.data", Matchers.is("사용 가능한 ID입니다.")));
    }
}
