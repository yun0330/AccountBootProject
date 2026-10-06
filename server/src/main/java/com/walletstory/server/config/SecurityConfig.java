package com.walletstory.server.config;

import com.walletstory.server.exception.SecurityAccessExceptionHandler;
import com.walletstory.server.exception.SecurityAuthException;
import com.walletstory.server.security.TokenVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenVerification tokenVerification;
    private final SecurityAuthException authException;
    private final SecurityAccessExceptionHandler accessExceptionHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/member/createuser",
                                "/member/checkid",
                                "/member/login",
                                "/member/refresh"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authException)
                        .accessDeniedHandler(accessExceptionHandler)
                )
                .addFilterBefore(tokenVerification, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
