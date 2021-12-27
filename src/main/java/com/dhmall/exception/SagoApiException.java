package com.dhmall.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class SagoApiException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
}
