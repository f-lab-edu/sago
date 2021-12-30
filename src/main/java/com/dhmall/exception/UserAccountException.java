package com.dhmall.exception;

import org.springframework.http.HttpStatus;

public class UserAccountException extends SagoApiException {
    public UserAccountException(HttpStatus status, ErrorCode code, String message) {
        super(status, code, message);
    }
}
