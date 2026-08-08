package com.tkdoon.ticket_app.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // 秘密鍵（本番では環境変数などで管理）
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT生成
    public static String generateToken(String userId) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600_000; // 1時間有効
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }
}
