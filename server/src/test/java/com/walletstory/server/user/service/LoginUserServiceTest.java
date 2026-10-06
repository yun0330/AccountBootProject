package com.walletstory.server.user.service;

import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.repository.UserRepository;
import com.walletstory.server.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("아이디가 없으면 예외가 발생한다.")
    void loginUserIdNotFound() {
        String userId = "wind123";
        String userPw = "1234";

        when(userRepository.findByUserId(userId)).thenReturn(null);

        assertThatThrownBy(() -> userService.getByCredentials(userId,userPw))
                .isInstanceOf(CustomException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");

        verify(userRepository).findByUserId(userId);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("비밀번호가 틀리면 예외가 발생한다.")
    void loginUserWrongPw() {
        String userId = "wind123";
        String userPw = "1234";
        String encoderPw = "$2a$10$encoderPw";

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .userPw(encoderPw)
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(passwordEncoder.matches(userPw, encoderPw)).thenReturn(false);

       assertThatThrownBy(() -> userService.getByCredentials(userId, userPw))
               .isInstanceOf(CustomException.class)
               .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");

       verify(userRepository).findByUserId(userId);
       verify(passwordEncoder).matches(userPw, encoderPw);
    }

    @Test
    @DisplayName("비밀번호가 맞으면 로그인 성공한다.")
    void loginSuccess() {
        String userId = "wind123";
        String userPw = "1234";
        String encoderPw = "$2a$10$encoderPw";

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .userPw(encoderPw)
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(passwordEncoder.matches(userPw, encoderPw)).thenReturn(true);

        UserEntity result = userService.getByCredentials(userId, userPw);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getUserPw()).isEqualTo(encoderPw);

        verify(userRepository).findByUserId(userId);
        verify(passwordEncoder).matches(userPw, encoderPw);
    }
}
