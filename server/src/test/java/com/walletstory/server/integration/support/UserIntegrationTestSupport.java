package com.walletstory.server.integration.support;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class UserIntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;

    protected UserEntity saveUser (
            String userId,
            String userPw
    ) {
        UserEntity user = UserEntity.builder()
                .userId(userId)
                .userPw(passwordEncoder.encode(userPw))
                .userName("홍길동")
                .userEmail("wind123@naver.com")
                .userPhone("01012345678")
                .nickName("날씨")
                .enrollDate(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    protected JsonNode login (
            String userId,
            String userPw
    ) throws Exception {
        UserDTO requestDTO = UserDTO.builder()
                .userId(userId)
                .userPw(userPw)
                .build();

        String responseBody = mockMvc.perform(post("/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(responseBody);
    }

    protected ResultActions requestRefresh(String refreshToken) throws Exception {
        UserDTO requestDTO = UserDTO.builder()
                .refreshToken(refreshToken)
                .build();

        return mockMvc.perform(post("/member/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));
    }
}
