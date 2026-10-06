package com.walletstory.server.security;

import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TokenProvisionTest {

    private static final String TESTSecretKey =
            "0123456789abcdef0123456789abcdef" + "0123456789abcdef0123456789abcdef";

    private static final long accessExpirationMs =
            Duration.ofMinutes(30).toMillis();

    private static final long refreshExpirationMs =
            Duration.ofDays(1).toMillis();

    private TokenProvision tokenProvision;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        tokenProvision = new TokenProvision(
                TESTSecretKey,
                accessExpirationMs,
                refreshExpirationMs
        );
        user = UserEntity.builder()
                .userId("wind123")
                .build();
    }

    @Test
    @DisplayName("access 토큰 생성후 access 타입으로 검증하면 userId를 반환한다.")
    void createAccessTokenAndValidate() {
        String token = tokenProvision.createAccessToken(user);
        String userId = tokenProvision.validateAndGetUserId(token, "access");

        assertThat(userId).isEqualTo("wind123");
    }

    @Test
    @DisplayName("refresh 토큰 생성후 refresh 타입으로 검증하면 userId를 반환한다.")
    void createRefreshTokenAndValidate() {
        String token = tokenProvision.createRefreshToken(user);
        String userId = tokenProvision.validateAndGetUserId(token, "refresh");

        assertThat(userId).isEqualTo("wind123");
    }

    @Test
    @DisplayName("access 토큰을 refresh 타입으로 검증하면 예외가 발생한다.")
    void accessTokenCannotBeUsedRefreshToken() {
        String accessToken = tokenProvision.createAccessToken(user);

        assertThatThrownBy(() -> tokenProvision.validateAndGetUserId(accessToken,"refresh"))
                .isInstanceOf(CustomException.class)
                .hasMessage("잘못된 토큰 타입 입니다.");
    }

    @Test
    @DisplayName("refresh 토큰을 access 타입으로 검증하면 예외가 발생한다.")
    void refreshTokenCannotBeUsedAccessToken() {
        String refreshToken = tokenProvision.createRefreshToken(user);

        assertThatThrownBy(()-> tokenProvision.validateAndGetUserId(refreshToken,"access"))
                .isInstanceOf(CustomException.class)
                .hasMessage("잘못된 토큰 타입 입니다.");
    }

    @Test
    @DisplayName("잘못된 토큰 문자열을 검증하면 예외가 발생한다.")
    void invalidTokenException() {
        String invalidToken = "invalid.jwt.token";

        assertThatThrownBy(() -> tokenProvision.validateAndGetUserId(invalidToken,"access"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("만료된 access 토큰을 검증하면 예외가 발생한다.")
    void expiredAccessTokenException() {
        TokenProvision expiredTokenProvision = new TokenProvision(
                TESTSecretKey,
                Duration.ofSeconds(-1).toMillis(),
                refreshExpirationMs
        );
        String expiredToken = expiredTokenProvision.createAccessToken(user);

        assertThatThrownBy(() -> expiredTokenProvision.validateAndGetUserId(expiredToken, "access"))
                .isInstanceOf(ExpiredJwtException.class);
    }

}
