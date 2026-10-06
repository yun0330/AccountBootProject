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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("아이디가 없으면 예외가 발생한다.")
    void createUserIdIsNull() {
        UserEntity user = UserEntity.builder()
                .userId(null)
                .userPw("1234")
                .build();

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(CustomException.class)
                .hasMessage("아이디를 입력해주세요.");

        verify(userRepository, never()).save(any());

    }

    @Test
    @DisplayName("이미 존재하는 아이디면 예외가 발생한다.")
    void createUserIdAlreadyExists() {
        UserEntity user = UserEntity.builder()
                .userId("wind123")
                .userPw("1234")
                .build();

        when(userRepository.existsById("wind123")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 사용중인 아이디입니다.");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("정상 회원이면 저장에 성공한다.")
    void createSuccess() {
        UserEntity user = UserEntity.builder()
                .userId("wind123")
                .userPw("1234")
                .userName("홍길동")
                .build();

        when(userRepository.existsById("wind123")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoderPw");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity savedUser = userService.create(user);

        assertThat(savedUser.getUserId()).isEqualTo("wind123");
        assertThat(savedUser.getUserPw()).isEqualTo("encoderPw");

        verify(userRepository).save(user);
    }




    @Test
    @DisplayName("회원가입시 비밀번호가 암호화된다.")
    void createEncryptPassword() {
        UserEntity user = UserEntity.builder()
                .userId("wind123")
                .userPw("originalPw")
                .build();

        when(userRepository.existsById("wind123")).thenReturn(false);
        when(passwordEncoder.encode("originalPw")).thenReturn("encoderPw");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity saveduser = userService.create(user);

        assertThat(saveduser.getUserPw()).isEqualTo("encoderPw");
        assertThat(saveduser.getUserPw()).isNotEqualTo("originalPw");

        verify(passwordEncoder).encode("originalPw");
        verify(userRepository).save(user);
    }

}
