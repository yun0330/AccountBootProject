package com.walletstory.server.integration.user;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tools.jackson.databind.JsonNode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginUserIntegrationTest extends UserCreateIntegrationTest {

    @Test
    @DisplayName("로그인에 성공하면 access 토큰과 refresh 토큰을 발급한다.")
    void loginSuccess() throws Exception{
        saveUser(
                "wind123",
                "1234"
        );

        JsonNode response = login("wind123","1234");

        String accessToken = response.get("accessToken").asString();
        String refreshToken = response.get("refreshToken").asString();

        assertThat(accessToken).isNotBlank();
        assertThat(refreshToken).isNotBlank();

        UserEntity saveUser = userRepository.findByUserId("wind123");

        assertThat(saveUser.getLoginToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("비밀번호가 올바르지 않으면 예외가 발생한다.")
    void loginUserWrongPw() throws Exception {
        saveUser(
                "wind123",
                "1234"
        );

        UserDTO requestDTO = UserDTO.builder()
                .userId("wind123")
                .userPw("12345")
                .build();

        mockMvc.perform(post("/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpectAll(status().isUnauthorized(),jsonPath("$.error")
                        .value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    @DisplayName("토큰 없이 보호된 API에 접근하면 예외가 발생한다.")
    void notAccessTokenApiNo() throws Exception {
        mockMvc.perform(get("/member/me")).andExpect(status().isUnauthorized());
    }
}
