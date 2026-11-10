package org.example.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. HTTP 요청 헤더에서 Authorization 값 가져오기
        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        // 2. Bearer 접두사를 제거하고 JWT 추출
        String token = getAccessToken(authorizationHeader);

        // 3. JWT가 유효하면 인증 정보 저장
        if (tokenProvider.validToken(token)) {
            Authentication authentication =
                    tokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        // 4. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {

        if (authorizationHeader != null
                && authorizationHeader.startsWith(TOKEN_PREFIX)) {

            return authorizationHeader.substring(
                    TOKEN_PREFIX.length()
            );
        }

        return null;
    }
}
