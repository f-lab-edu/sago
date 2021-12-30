package com.dhmall.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorModel {
    private final ErrorCode code;
    private final String message;
}
