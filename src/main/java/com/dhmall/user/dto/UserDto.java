package com.dhmall.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.time.ZonedDateTime;

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
    @Email(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "유효하지 않은 이메일 주소입니다.")
    @Size(max = 30)
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(max = 30)
    private String password;

    @NotEmpty(message = "이름을 입력해주세요.")
    @Size(max = 30)
    private String name;

    @NotEmpty(message = "핸드폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "유효하지 않은 핸드폰 번호입니다.")
    @Size(max = 15)
    private String phoneNumber;

    @NotEmpty(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^(\\d{4}).(0[0-9]|1[0-2]).(0[1-9]|[1-2][0-9]|3[0-1])$", message = "유효하지 않은 생년월일입니다.")
    @Size(max = 10)
    private String birth;

    private int authStatus;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
