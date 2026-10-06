package com.walletstory.server.security;


import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenVerificationTest {
    @Mock
    private TokenProvision tokenProvision;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TokenVerification tokenVerification;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 인증하지 않고 다음 필터로 진행한다.")
    void authorizationHeaderNotFound() throws Exception {
        tokenVerification.doFilter(
                request,
                response,
                filterChain
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNull();

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenProvision, userRepository);
    }

    @Test
    @DisplayName("유효한 access 토큰이면 securityContext에 인증을 저장한다.")
    void validAccessToken() throws Exception {
        String accessToken = "validAccessToken";
        String userId = "wind123";

        request.addHeader(
                "Authorization",
                "Bearer " + accessToken
        );

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .build();

        when(tokenProvision.validateAndGetUserId(accessToken, "access"))
                .thenReturn(userId);

        when(userRepository.findByUserId(userId))
                .thenReturn(user);

        tokenVerification.doFilter(
                request,
                response,
                filterChain
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userId);
        assertThat(authentication.getCredentials()).isNull();
        assertThat(authentication.isAuthenticated()).isTrue();

        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");

        verify(tokenProvision).validateAndGetUserId(accessToken, "access");

        verify(userRepository).findByUserId(userId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("refresh 토큰을 Authorization 헤더에 넣으면 인증하지 않는다.")
    void refreshTokenCannotAuthenticate() throws Exception{
        String refreshToken = "refreshToken";

        request.addHeader(
                "Authorization",
                "Bearer " + refreshToken
        );

        when(tokenProvision.validateAndGetUserId(refreshToken, "access"))
                .thenThrow(new CustomException("잘못된 토큰 타입입니다.", HttpStatus.UNAUTHORIZED));

        tokenVerification.doFilter(
                request,
                response,
                filterChain
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNull();

        verify(tokenProvision).validateAndGetUserId(refreshToken, "access");

        verifyNoInteractions(userRepository);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("토큰의 userId가 DB에 없으면 인증하지 않는다.")
    void tokenUserIdNotFound() throws Exception{
        String accessToken = "validAccessToken";
        String userId = "wind123";

        request.addHeader(
                "Authorization",
                "Bearer " + accessToken
        );

        when(tokenProvision.validateAndGetUserId(accessToken, "access"))
                .thenReturn(userId);

        when(userRepository.findByUserId(userId))
                .thenReturn(null);

        tokenVerification.doFilter(
                request,
                response,
                filterChain
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNull();

        verify(tokenProvision).validateAndGetUserId(accessToken, "access");

        verify(userRepository).findByUserId(userId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("만료된 토큰이면 SecurityContext를 비운다.")
    void expiredToken() throws Exception{
        String expiredToken = "expiredAccessToken";

        request.addHeader(
                "Authorization",
                "Bearer " + expiredToken
        );

        ExpiredJwtException expiredJwtException = mock(ExpiredJwtException.class);

        when(tokenProvision.validateAndGetUserId(expiredToken, "access"))
                .thenThrow(expiredJwtException);

        tokenVerification.doFilter(
                request,
                response,
                filterChain
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNull();

        verify(tokenProvision).validateAndGetUserId(expiredToken, "access");
        verifyNoInteractions(userRepository);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("조작된 토큰이면 securityContext를 비운다.")
    void manipulatedToken() throws Exception{
        String manipulatedToken = "manipulatedAccessToken";

        request.addHeader(
                "Authorization",
                "Bearer " + manipulatedToken
        );

        when(tokenProvision.validateAndGetUserId(manipulatedToken, "access"))
                .thenThrow(new JwtException("JWT 서명이 올바르지 않습니다."));

        tokenVerification.doFilter(
                request,
                response,
                filterChain
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNull();

        verify(tokenProvision).validateAndGetUserId(manipulatedToken, "access");

        verifyNoInteractions(userRepository);
        verify(filterChain).doFilter(request, response);
    }
}
