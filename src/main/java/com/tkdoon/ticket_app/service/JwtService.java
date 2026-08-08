package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.security.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    // シークレットキー（本番では環境変数などで管理推奨）
    private String secret = "jf8d9s7f98d7f9sd8f7ds9f8sd7f9s8df7s9d8f7sd98f7s9df87s9dx";
    private Key key;

    // トークンの有効期限（例：1時間）
    private final long expirationMs = 60 * 60 * 1000;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // JWTの生成（id, role を claims に含める）
    public String generateToken(String email,int id, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWTの検証
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // JWTから認証情報を作成（DBアクセスなし）
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        int id = claims.get("id", Integer.class);
        String role = claims.get("role", String.class);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if ("admin".equals(role)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        AuthUser userDetails = new AuthUser(id, email, role, authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
