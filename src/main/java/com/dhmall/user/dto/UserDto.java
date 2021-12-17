package com.dhmall.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Getter
@Setter
/* Jackson JSON 라이브러리를 활용하여 HTTP 통신을 할 때, 필드값을 매개변수로 받는 생성자를 선언해주어야 JSON 데이터 타입으로 형변환이 가능.
* @Builder 어노테이션을 활용하면 deserialize exception 발생!(참고: https://www.baeldung.com/jackson-exception)
*  */
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @Id
    private BigInteger id;

    @NotEmpty(message = "아이디를 입력해주세요.")
    @Size(max = 30)
    private String nickname;

    @NotEmpty(message = "이메일 계정을 입력해주세요.")
    @Email(regexp = "${regex.email}", message = "유효하지 않은 이메일 주소입니다.")
    @Size(max = 30)
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(max = 30)
    private String password;

    @NotEmpty(message = "이름을 입력해주세요.")
    @Size(max = 30)
    private String name;

    @NotEmpty(message = "핸드폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\\\d{3}|\\\\d{4})-\\\\d{4}$")
    @Size(max = 15)
    private String phoneNumber;

    @NotEmpty(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^(\\\\d{4}).(0[0-9]|1[0-2]).(0[1-9]|[1-2][0-9]|3[0-1])$")
    @Size(max = 10)
    private String birth;

    private String authKey;

    private int authStatus;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address;

    private String createdAt;

    private String updatedAt;
}
