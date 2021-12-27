package com.dhmall.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto {
    @NotEmpty(message = "아이디를 입력해주세요.")
    @Size(max = 30)
    private String nickname;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
}
