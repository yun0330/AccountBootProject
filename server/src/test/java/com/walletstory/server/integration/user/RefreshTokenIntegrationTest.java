package com.walletstory.server.integration.user;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RefreshTokenIntegrationTest extends UserCreateIntegrationTest {
    @Test
    @DisplayName("재발급 후 기존에 refresh 토큰을 재사용하면 예외를 반환한다.")
    void rejectOldRefreshToken() throws Exception {
        saveUser(
                "wind123",
                "1234"
        );

        String oldRefreshToken = login("wind123","1234")
                .required("refreshToken").asString();

        String responseBody = requestRefresh(oldRefreshToken)
                .andExpectAll(status().isOk(),
                        jsonPath("$.accessToken").isNotEmpty(),
                        jsonPath("$.refreshToken").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String newRefreshToken = objectMapper
                .readTree(responseBody)
                .required("refreshToken")
                .asString();

        assertThat(newRefreshToken).isNotEqualTo(oldRefreshToken);

        requestRefresh(oldRefreshToken).andExpect(status().isUnauthorized());

//        requestRefresh(newRefreshToken).andExpect(status().isOk());
    }
}
