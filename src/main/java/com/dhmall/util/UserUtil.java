package com.dhmall.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Slf4j
public class UserUtil {

    /**
     * Hash Algorithm: BCrypt
     * @param password
     * @return encrypted password
     */
    public static String encryptInfo(String password) {
        String raw = password;
        String salt = BCrypt.gensalt(16);
        String hashed = BCrypt.hashpw(raw, salt);

        return hashed;
    }

    public static boolean verifyEncryption(String raw, String hashed) {

        return BCrypt.checkpw(raw, hashed);
    }
}
