package com.example.pcmallcommon.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final byte[] KEY = "Teriri".getBytes();

    public static String createToken(Map<String, Object> payload, int offsetTime) {
        Date now = new Date();
        return JWT.create()
                .setKey(KEY)
                .addPayloads(payload)
                .setIssuedAt(now)
                .setExpiresAt(DateUtil.offsetDay(now, offsetTime))
                .sign();
    }

    public static JWT parseToken(String token) {
        return JWTUtil.parseToken(token);
    }

    public static String getPayload(JWT jwt, String key) {
        return (String) jwt.getPayload(key);
    }

    public static boolean verify(JWT jwt) {
        return !jwt.setKey(KEY).validate(0);
    }
}
