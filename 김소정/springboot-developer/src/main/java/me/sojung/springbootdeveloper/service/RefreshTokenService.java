package me.sojung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.sojung.springbootdeveloper.domain.RefreshToken;
import me.sojung.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new IllegalArgumentException("Unexpected token"));
    }

    public void deleteByUserId(Long userId){
        // 사용자 ID로 리프레시 토큰 조회
        Optional<RefreshToken> result = refreshTokenRepository.findByUserId(userId);
        // 있으면 삭제하고, 없으면 그대로 종료
        if(result.isPresent()){
            refreshTokenRepository.delete(result.get());
        }
    }
}
