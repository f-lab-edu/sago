package com.dhmall.util;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class UserUtil {

    @SneakyThrows
    public static StringBuffer encryptInfo(String password, String authKey) {
        String raw = password + authKey;
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(raw.getBytes());
        byte[] digested = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digested.length; ++i) {
            sb.append(Integer.toHexString(0xff & digested[i]));
        }
        return sb;
    }
}
