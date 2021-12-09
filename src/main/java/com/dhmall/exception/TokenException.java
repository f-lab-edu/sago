package com.dhmall.exception;

import io.jsonwebtoken.JwtException;

public class TokenException extends JwtException {
    public TokenException(String message) {
        super(message);
    }
}
