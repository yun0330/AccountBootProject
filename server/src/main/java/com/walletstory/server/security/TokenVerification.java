package com.walletstory.server.security;

import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenVerification extends OncePerRequestFilter {

    private final TokenProvision tokenProvision;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = parseBearerToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String userId = tokenProvision.validateAndGetUserId(token, "access");
            UserEntity user = userRepository.findByUserId(userId);

            if (user == null) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user.getUserId(),
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_USER")
            );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료");
            SecurityContextHolder.clearContext();
        } catch (CustomException e) {
            log.warn("JWT 인증 조건 불일치:{}",e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT 인증 실패: {}",e.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
