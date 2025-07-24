package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;


public class SocketJwtAuthentication {
    public static Integer getUserId(String token) {
        token = token.trim();
        Integer userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
