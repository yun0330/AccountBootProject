package com.walletstory.server.user.controller;


import com.walletstory.server.controller.UserController;
import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.exception.GlobalExceptionHandler;
import com.walletstory.server.repository.UserRepository;
import com.walletstory.server.security.TokenProvision;
import com.walletstory.server.security.TokenVerification;
import com.walletstory.server.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenProvision tokenProvision;

    @MockitoBean
    private TokenVerification tokenVerification;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입에 성공하면 200과 사용자 id를 반환한다.")
    void createUserSuccess() throws Exception {
        UserEntity savedUser = UserEntity.builder()
                .userId("wind123")
                .userPw("encoderPw")
                .build();

        when(passwordEncoder.encode("1234"))
                .thenReturn("encoderPw");

        when(userService.create(any(UserEntity.class)))
                .thenReturn(savedUser);

        mockMvc.perform(post("/member/createuser")
                .contentType("application/json")
                .content("""
                        {
                        "userId": "wind123",
                        "userPw": "1234",
                        "userEmail": "wind123@naver.com",
                        "userName": "홍길동",
                        "userPhone": "01012345678",
                        "nickName": "날씨"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("wind123"));
    }

    @Test
    @DisplayName("아이디가 존재하면 true를 반환한다.")
    void checkUserIdExists() throws Exception {
        when(userService.existsByUserId("wind123"))
                .thenReturn(true);

        mockMvc.perform(get("/member/checkid")
                .param("userId", "wind123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("아이디가 존재하지 않으면 false를 반환한다.")
    void checkUserIdNotExists() throws Exception {
        when(userService.existsByUserId("wind123"))
                .thenReturn(false);

        mockMvc.perform(get("/member/checkid")
                .param("userId", "wind123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    @DisplayName("로그인 성공 시 사용자 id와 토큰을 반환한다.")
    void loginSuccess() throws Exception {
        UserEntity user = UserEntity.builder()
                .userId("wind123")
                .userPw("encoderPw")
                .build();

        when(userService.getByCredentials(
                "wind123",
                "1234"
        )).thenReturn(user);

        when(tokenProvision.createAccessToken(user))
                .thenReturn("accessToken");
        when(tokenProvision.createRefreshToken(user))
                .thenReturn("refreshToken");

        mockMvc.perform(post("/member/login")
                .contentType("application/json")
                .content("""
                        {
                        "userId": "wind123",
                        "userPw": "1234"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("wind123"))
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
        verify(userService).updateUser(user);
    }

    @Test
    @DisplayName("로그인 실패 시 예외를 반환한다.")
    void loginFailure() throws Exception {
        when(userService.getByCredentials(
                "wind123",
                "1234"
        )).thenThrow(new CustomException("아이디 또는 비밀번호가 올바르지 않습니다.",
                HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/member/login")
                .contentType("application/json")
                .content("""
                        {
                        "userId": "wind123",
                        "userPw": "1234"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    @DisplayName("refresh 토큰이 유효하면 새로운 토큰을 반환한다.")
    void refreshSuccess() throws Exception{
        UserDTO responseDTO = UserDTO.builder()
                .userId("wind123")
                .accessToken("newAccessToken")
                .refreshToken("newRefreshToken")
                .build();

        when(userService.refreshToken("oldRefreshToken"))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/member/refresh")
                .contentType("application/json")
                .content("""
                        {
                        "refreshToken": "oldRefreshToken"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("wind123"))
                .andExpect(jsonPath("$.accessToken")
                        .value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("newRefreshToken"));
    }

    @Test
    @DisplayName("refresh 토큰이 유효하지 않으면 예외를 반환한다.")
    void refreshFailure() throws Exception {
        when(userService.refreshToken("invalidRefreshToken"))
                .thenThrow(new CustomException("Refresh 토큰이 유효하지 않습니다.",
                        HttpStatus.UNAUTHORIZED
                        ));

        mockMvc.perform(post("/member/refresh")
                .contentType("application/json")
                .content("""
                        {
                        "refreshToken": "invalidRefreshToken"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("Refresh 토큰이 유효하지 않습니다."));
    }

}
