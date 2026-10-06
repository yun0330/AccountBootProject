package com.walletstory.server.integration.user;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.integration.support.UserIntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserCreateIntegrationTest extends UserIntegrationTestSupport {

    private ResultActions createUser(UserDTO request) throws Exception {
        return  mockMvc.perform(post("/member/createuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Test
    @DisplayName("회원가입에 성공하면 비밀번호가 암호화되어 저장한다.")
    void createUserSuccess() throws Exception {
        createUser(createRequest(
                "wind123",
                "wind123@naver.com"
        )).andExpectAll(status().isOk(),
                jsonPath("$.userId").value("wind123")
        );

        assertThat(userRepository.findByUserId("wind123"))
                .isNotNull()
                .satisfies(user -> {
                    assertThat(user.getUserPw()).isNotEqualTo("1234");
                    assertThat(passwordEncoder.matches("1234",user.getUserPw())).isTrue();
                    assertThat(user.getEnrollDate()).isNotNull();
                });
    }

    @Test
    @DisplayName("이미 사용중인 아이디로 회원가입하면 예외가 발생한다.")
    void createUserIdAlreadyExists() throws Exception {
        saveUser("wind123", "1234");
        createUser(createRequest(
                "wind123",
                "wind123@naver.com"
        )).andExpectAll(status().isConflict(),jsonPath("$.error").exists());
    }

    private UserDTO createRequest(
            String userId,
            String userEmail
    ) {
        return UserDTO.builder()
                .userId(userId)
                .userPw("1234")
                .userName("홍길동")
                .userEmail(userEmail)
                .userPhone("01012345678")
                .nickName("날씨")
                .build();
    }
}
