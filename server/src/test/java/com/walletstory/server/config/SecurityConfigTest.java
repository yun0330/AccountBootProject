package com.walletstory.server.config;

import com.walletstory.server.exception.SecurityAccessExceptionHandler;
import com.walletstory.server.exception.SecurityAuthException;
import com.walletstory.server.repository.UserRepository;
import com.walletstory.server.security.TokenProvision;
import com.walletstory.server.security.TokenVerification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SecurityConfigTest.TestController.class)
@Import({
        SecurityConfig.class,
        TokenVerification.class,
        SecurityAuthException.class,
        SecurityAccessExceptionHandler.class,
        SecurityConfigTest.TestController.class
})
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenProvision tokenProvision;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 API는 인증 없이 접근할 수 있다.")
    void createUserPermitAll() throws Exception {
        mockMvc.perform(post("/member/createuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    @DisplayName("아이디 중복 확인 API는 인증 없이 접근할 수 있다.")
    void checkUserIdPermitAll() throws Exception {
        mockMvc.perform(get("/member/checkid"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    @DisplayName("토큰 재발급 API는 인증 없이 접근할 수 있다.")
    void refreshPermitAll() throws Exception {
        mockMvc.perform(post("/member/refresh"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    @DisplayName("보호된 API는 인증 정보가 없으면 예외를 반환한다.")
    void privateApiNotCredentialsException() throws Exception {
        mockMvc.perform(get("/private/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("인증이 필요합니다."));
    }

    @Test
    @WithMockUser(
            username = "wind1234",
            roles = "USER"
    )
    @DisplayName("인증된 사용자는 보호된 API에 접근할 수 있다.")
    void privateApiVerifiedUserOk() throws Exception {
        mockMvc.perform(get("/private/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @RestController
    public static class TestController {

        @PostMapping("/member/createuser")
        public String createUser() {
            return "ok";
        }

        @GetMapping("/member/checkid")
        public String checkUserId() {
            return "ok";
        }

        @PostMapping("/member/login")
        public String loginUser() {
            return "ok";
        }

        @PostMapping("/member/refresh")
        public String refresh() {
            return "ok";
        }

        @GetMapping("/private/test")
        public String privateApi() {
            return "ok";
        }
    }
}
