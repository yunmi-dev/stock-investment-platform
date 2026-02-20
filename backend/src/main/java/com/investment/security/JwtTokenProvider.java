package com.investment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 토큰 생성 / 검증 담당
 *
 * JWT 구조: header.payload.signature
 *   header    → 알고리즘 정보 (HS256)
 *   payload   → 실제 데이터 (email, 만료시간)
 *   signature → 위변조 방지 서명
 */
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {
        // 문자열 시크릿 → SecretKey 객체로 변환
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /**
     * 토큰 생성
     * 로그인/회원가입 성공 시 이 토큰을 클라이언트에게 반환
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)        // payload에 email 저장
                .issuedAt(now)         // 발급 시간
                .expiration(expiry)    // 만료 시간
                .signWith(secretKey)   // 서명 (위변조 방지)
                .compact();
    }

    /**
     * 토큰에서 email 추출
     * 요청마다 "이 토큰이 누구 것인지" 확인할 때 사용
     */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getPayload().getSubject();
    }

    /**
     * 토큰 유효성 검증
     * 만료됐거나, 서명이 잘못됐거나, 형식이 이상하면 false
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}
