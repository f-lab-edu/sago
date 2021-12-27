package com.dhmall.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class SagoApiResponse<T> {
    private final HttpStatus responseCode;
    private final T data;
}
