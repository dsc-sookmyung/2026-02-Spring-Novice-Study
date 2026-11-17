package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.jwt.TokenProvider;
import org.example.domain.RefreshToken;
import org.example.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {

        // 1. 리프레시 토큰의 유효성 검사
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        // 2. DB에서 리프레시 토큰 조회
        Long userId = refreshTokenService
                .findByRefreshToken(refreshToken)
                .getUserId();

        // 3. 사용자 정보 조회
        User user = userService.findById(userId);

        // 4. 새로운 액세스 토큰 생성
        return tokenProvider.generateToken(
                user,
                Duration.ofHours(2)
        );
    }
}
