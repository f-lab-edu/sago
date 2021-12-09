package com.dhmall.user.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Getter
@Setter
@Builder
public class UserDto {

    @Id
    private BigInteger id;

    @NotEmpty(message = "아이디를 입력해주세요.")
    @Size(max = 30)
    private String nickname;

    @NotEmpty(message = "이메일 계정을 입력해주세요.")
//    @Email(regexp = "^([0-9a-zA-Z]*)@([a-z])", message = "유효하지 않은 이메일 주소입니다.")
    @Size(max = 30)
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(max = 30)
    private String password;

    @NotEmpty(message = "이름을 입력해주세요.")
    @Size(max = 30)
    private String name;

    @NotEmpty(message = "핸드폰 번호를 입력해주세요.")
    @Size(max = 15)
    private String phoneNumber;

    @NotEmpty(message = "생년월일을 입력해주세요.")
    @Size(max = 10)
    private String birth;

    private String authKey;

    private int authStatus;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address;

    private String createdAt;

    private String updatedAt;
}
