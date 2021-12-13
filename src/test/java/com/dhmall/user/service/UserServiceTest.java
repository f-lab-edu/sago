package com.dhmall.user.service;

import com.dhmall.user.dto.UserDto;
import com.dhmall.user.mapper.UserMapper;
import com.dhmall.util.SecurityUtil;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigInteger;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
public class UserServiceTest {

    private final UserMapper userMapper = mock(UserMapper.class);
    private final EmailService emailService = mock(EmailService.class);
    private final UserService userService = new UserService(userMapper, emailService);
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static UserDto expected;

    @BeforeEach
    void createObjectEachTime() {
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
    }

    @AfterAll
    public static void closeBeanObject() {
        validatorFactory.close();
    }

    @Test
    void registerValidUser() {
        // when
        doNothing().when(userMapper).insertUser(any());
        /* UserService static class injection */
        /** TODO: static method 리턴값으로 암호화된 비밀번호 얻는 방식 알아보기 **/
        MockedStatic<SecurityUtil> securityUtilMockedStatic = mockStatic(SecurityUtil.class);
        UserDto actual = userService.registerUser(expected);

        //then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNickname(), actual.getNickname());
        assertEquals(expected.getEmail(), actual.getEmail());
//        assertEquals(expected.getPassword(), "TBD");
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getBirth(), actual.getBirth());
        assertEquals(expected.getAuthKey(), actual.getAuthKey());
        assertEquals(expected.getAuthStatus(), actual.getAuthStatus());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
    }

    @Test
    void registerUserWithNullValue() {
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
    void registerUserWithInvalidValue() {
        // given - invalid email
        UserDto invalidEmail = expected;

        // when - invalid email

        // then - invalid email

        // given - invalid phone number
        UserDto invalidPhoneNumber = expected;

        // when - invalid phone number

        // then - invalid phone number


        // given - invalid birth
        UserDto invalidBirth = expected;

        // when - invalid birth

        // then - invalid birth


    }
}
