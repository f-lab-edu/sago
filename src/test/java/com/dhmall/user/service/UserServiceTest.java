package com.dhmall.user.service;

import com.dhmall.exception.UserAccountException;
import com.dhmall.user.dto.UserDto;
import com.dhmall.user.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.*;

import com.dhmall.util.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigInteger;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PrepareForTest({SecurityUtil.class})
public class UserServiceTest {

    private final UserMapper userMapper = mock(UserMapper.class);
    private final EmailService emailService = mock(EmailService.class);
    private final UserService userService = new UserService(userMapper, emailService);
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static UserDto expected;

    @BeforeEach
    public void createObjectEachTime() {
        // given in common
        BigInteger id = new BigInteger("1");
        String nickname = "pranne1224";
        String email = "it_developer1224@naver.com";
        String password = "validtest";
        String name = "Dahye Lee";
        String phoneNumber = "010-2321-2123";
        String birth = "1999.12.25";
        String address = "Seoul";

        expected = new UserDto();
        expected.setId(id);
        expected.setNickname(nickname);
        expected.setEmail(email);
        expected.setPassword(password);
        expected.setName(name);
        expected.setPhoneNumber(phoneNumber);
        expected.setBirth(birth);
        expected.setAddress(address);
    }

    @BeforeAll
    public static void createBeanObject() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void closeBeanObject() {
        validatorFactory.close();
    }

    @Test
    void registerValidUser() {
        // when
        doNothing().when(userMapper).insertUser(any());
        UserDto actual = userService.registerUser(expected);

        //then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNickname(), actual.getNickname());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getBirth(), actual.getBirth());
        assertEquals(expected.getAuthStatus(), actual.getAuthStatus());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
    }

    @Test
    void registerUserWithNullUser() {
        // given
        UserDto nullNickname = expected;
        UserDto nullEmail = expected;
        UserDto nullPassword = expected;
        UserDto nullName = expected;
        UserDto nullPhoneNumber = expected;
        UserDto nullBirth = expected;
        UserDto nullAddress = expected;

        nullNickname.setNickname("");
        nullEmail.setEmail("");
        nullPassword.setPassword("");
        nullName.setName("");
        nullPhoneNumber.setPhoneNumber("");
        nullBirth.setBirth("");
        nullAddress.setAddress("");

        // when
        Set<ConstraintViolation<UserDto>> nicknameViolation = validator.validate(nullNickname);
        Set<ConstraintViolation<UserDto>> emailViolation = validator.validate(nullEmail);
        Set<ConstraintViolation<UserDto>> passwordViolation = validator.validate(nullPassword);
        Set<ConstraintViolation<UserDto>> nameViolation = validator.validate(nullName);
        Set<ConstraintViolation<UserDto>> phoneNumberViolation = validator.validate(nullPhoneNumber);
        Set<ConstraintViolation<UserDto>> birthViolation = validator.validate(nullBirth);
        Set<ConstraintViolation<UserDto>> addressViolation = validator.validate(nullAddress);

        // then
        assertFalse(nicknameViolation.isEmpty());
        assertFalse(emailViolation.isEmpty());
        assertFalse(passwordViolation.isEmpty());
        assertFalse(nameViolation.isEmpty());
        assertFalse(phoneNumberViolation.isEmpty());
        assertFalse(birthViolation.isEmpty());
        assertFalse(addressViolation.isEmpty());

    }

    @Test
    void registerUserWithInvalidUser() {
        // given - invalid email
        UserDto invalidEmail = expected;
        invalidEmail.setEmail("email@gmailcom");

        // when - invalid email
        Set<ConstraintViolation<UserDto>> emailViolation = validator.validate(invalidEmail);

        // then - invalid email
        assertFalse(emailViolation.isEmpty());

        // given - invalid phone number
        UserDto invalidPhoneNumber = expected;
        invalidPhoneNumber.setPhoneNumber("111-223.1232");

        // when - invalid phone number
        Set<ConstraintViolation<UserDto>> phoneNumberViolation = validator.validate(invalidPhoneNumber);

        // then - invalid phone number
        assertFalse(phoneNumberViolation.isEmpty());

        // given - invalid birth
        UserDto invalidBirth = expected;
        invalidBirth.setBirth("2001-12.12");

        // when - invalid birth
        Set<ConstraintViolation<UserDto>> birthViolation = validator.validate(invalidBirth);

        // then - invalid birth
        assertFalse(birthViolation.isEmpty());
    }

    @Test
    void checkDuplicateIdWithValidId() {
        // when
        String actual = userService.checkDuplicateId(expected.getNickname());

        // then
        assertEquals(expected.getNickname(), actual);
    }

    @Test
    void checkDuplicateIdWithInvalidId() {
        // when
        Exception actual = assertThrows(UserAccountException.class, () -> {
            when(userMapper.findById(anyString())).thenReturn(expected);
            userService.checkDuplicateId(anyString());
        });

        // then
        String expectedMessage = "이미 등록된 아이디입니다.";
        String actualMessage = actual.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void loginUnregisteredUser() {
        // given
        String nickname = "newUser";
        String password = "Welcome!";

        // when
        Exception actual = assertThrows(UserAccountException.class, () -> {
            when(userMapper.findById(nickname)).thenReturn(null);
            userService.login(nickname, password);
        });

        // then
        String expectedMessage = "등록되지 않은 계정입니다.";
        String actualMessage = actual.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void loginUnVerifiedUser() {
        // given
        String nickname = "sagotest4";
        String password = "test4";
        UserDto unVerified = new UserDto();
        unVerified.setNickname(nickname);
        unVerified.setPassword(password);
        unVerified.setAuthStatus(0);

        // when
        Exception actual = assertThrows(UserAccountException.class, () -> {
            when(userMapper.findById(nickname)).thenReturn(unVerified);
            userService.login(nickname, password);
        });

        // then
        String expectedMessage = "이메일 인증이 필요합니다. 이메일 인증 여부를 확인해주세요.";
        String actualMessage = actual.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void loginAccountNicknameUnmatched() {
        // given
        String nickname = "sagotest2";
        String password = "test10";
        UserDto unmatchedNickname = new UserDto();
        unmatchedNickname.setNickname("sagotest1");
        unmatchedNickname.setPassword(password);
        unmatchedNickname.setAuthStatus(1);

        // when
        Exception actual = assertThrows(UserAccountException.class, () -> {
            when(userMapper.findById(nickname)).thenReturn(unmatchedNickname);
            userService.login(nickname, password);
        });

        // then
        String expectedMessage = "입력하신 아이디를 확인해주세요.";
        String actualMessage = actual.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void loginAccountPasswordUnmatched() {
        // given
        String nickname = "sagotest2";
        String password = "test10";
        UserDto unmatchedPasswd = new UserDto();
        unmatchedPasswd.setNickname(nickname);
        unmatchedPasswd.setPassword("test20");
        unmatchedPasswd.setAuthStatus(1);

        // when
        Exception actual = assertThrows(UserAccountException.class, () -> {
            when(userMapper.findById(nickname)).thenReturn(unmatchedPasswd);
            userService.login(nickname, password);
        });

        // then
        String expectedMessage = "입력하신 비밀번호를 확인해주세요.";
        String actualMessage = actual.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
