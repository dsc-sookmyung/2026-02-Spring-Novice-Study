package me.yuninyeong.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.yuninyeong.springbootdeveloper.domain.RefreshToken;
import me.yuninyeong.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
