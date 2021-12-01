package com.dhmall.user.dto;

import lombok.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class UserDto {

    private BigInteger id;

    private String userId;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String birth;

    private String authKey;

    private int authStatus;

    private String address;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
