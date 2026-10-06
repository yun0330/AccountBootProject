package com.walletstory.server.user.service;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.repository.UserRepository;
import com.walletstory.server.security.TokenProvision;
import com.walletstory.server.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvision tokenProvision;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("refresh 토큰이 없으면 예외가 발생한다.")
    void notRefreshTokenUnauthorized() {
        CustomException exception = assertThrows(
                CustomException.class,
                () -> userService.refreshToken(null)
        );

        assertThat(exception.getMessage()).isEqualTo("Refresh 토큰이 없습니다.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("refresh 토큰 타입이 access면 예외가 발생한다.")
    void refreshTokenDifferentTypeAccessToken() {
        String accessToken = "accessToken";

        when(tokenProvision.validateAndGetUserId(accessToken, "refresh"))
                .thenThrow(new CustomException("잘못된 토큰 타입 입니다.", HttpStatus.UNAUTHORIZED));

        CustomException exception = assertThrows(
                CustomException.class,
                () -> userService.refreshToken(accessToken)
        );

        assertThat(exception.getMessage()).isEqualTo("잘못된 토큰 타입 입니다.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("DB에 refresh 토큰과 요청 refresh 토큰이 다르면 DB에 저장된 토큰을 삭제하고 예외를 발생시킨다.")
    void  refreshTokenRemoveStoredTokenAndUnauthorized() {
        String oldRefreshToken = "oldRefreshToken";
        String saveRefreshToken = "saveRefreshToken";
        String userId = "wind123";

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .loginToken(saveRefreshToken)
                .build();

        when(tokenProvision.validateAndGetUserId(oldRefreshToken, "refresh"))
                .thenReturn(userId);
        when(userRepository.findByUserId(userId))
                .thenReturn(user);

        CustomException exception = assertThrows(
                CustomException.class,
                () -> userService.refreshToken(oldRefreshToken)
        );

        assertThat(exception.getMessage()).isEqualTo("Refresh 토큰이 유효하지 않습니다.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(user.getLoginToken()).isNull();
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("refresh 토큰 검증 성공 시 새 access 토큰과 refresh 토큰을 반환하고 DB를 갱생한다.")
    void refreshTokenSuccessNewTokenUpdateDb() {
        String oldRefreshToken = "oldRefreshToken";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";
        String userId = "wind123";

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .loginToken(oldRefreshToken)
                .build();

        when(tokenProvision.validateAndGetUserId(oldRefreshToken, "refresh"))
                .thenReturn(userId);
        when(userRepository.findByUserId(userId))
                .thenReturn(user);
        when(tokenProvision.createAccessToken(user))
                .thenReturn(newAccessToken);
        when(tokenProvision.createRefreshToken(user))
                .thenReturn(newRefreshToken);

        UserDTO result = userService.refreshToken(oldRefreshToken);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(result.getRefreshToken()).isEqualTo(newRefreshToken);

        assertThat(user.getLoginToken()).isEqualTo(newRefreshToken);
        verify(userRepository).save(user);
    }
}
