package com.walletstory.server.security;

import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class TokenProvision {

    private final String secretKey;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public TokenProvision(
    @Value("${jwt.secret}") String secretKey,

    @Value("${jwt.access-expiration-ms}") long accessExpirationMs,

    @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs
) {
        this.secretKey = secretKey;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String createAccessToken(UserEntity userEntity) {
        return createToken(userEntity.getUserId(), accessExpirationMs, "access");
    }

    public String createRefreshToken(UserEntity userEntity) {
        return createToken(userEntity.getUserId(), refreshExpirationMs, "refresh");
    }

    private String createToken(String userId, long expirationMs, String type) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(userId)
                .claim("type",type)
                .setIssuer("walletstory-server")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String validateAndGetUserId(String token, String expectedType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String type = claims.get("type", String.class);

        if (!expectedType.equals(type)) {
            throw new CustomException("잘못된 토큰 타입 입니다.", HttpStatus.UNAUTHORIZED);
        }
        return claims.getSubject();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
