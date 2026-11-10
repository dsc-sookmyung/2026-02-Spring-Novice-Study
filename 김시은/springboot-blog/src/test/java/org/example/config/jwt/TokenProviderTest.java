package org.example.config.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    // 1. 토큰 생성 테스트
    @DisplayName("generateToken(): 유저 정보와 만료 기간으로 토큰을 생성한다.")
    @Test
    void generateToken() {
        User testUser = userRepository.save(
                User.builder()
                        .email("user@email.com")
                        .password("test")
                        .build()
        );

        String token = tokenProvider.generateToken(
                testUser, Duration.ofDays(14)
        );

        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    // 2. 만료된 토큰 테스트
    @DisplayName("validToken(): 만료된 토큰은 유효하지 않다.")
    @Test
    void validToken_invalidToken() {
        String token = JwtFactory.builder()
                .expiration(new Date(
                        new Date().getTime()
                                - Duration.ofDays(7).toMillis()
                ))
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isFalse();
    }

    // 3. 유효한 토큰 테스트
    @DisplayName("validToken(): 유효한 토큰은 true를 반환한다.")
    @Test
    void validToken_validToken() {
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isTrue();
    }

    // 4. 인증 정보 추출 테스트
    @DisplayName("getAuthentication(): 토큰에서 인증 정보를 가져온다.")
    @Test
    void getAuthentication() {
        String userEmail = "user@email.com";

        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        Authentication authentication =
                tokenProvider.getAuthentication(token);

        assertThat(
                ((UserDetails) authentication.getPrincipal())
                        .getUsername()
        ).isEqualTo(userEmail);
    }

    // 5. 사용자 ID 추출 테스트
    @DisplayName("getUserId(): 토큰에서 사용자 ID를 가져온다.")
    @Test
    void getUserId() {
        Long userId = 1L;

        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        Long userIdByToken = tokenProvider.getUserId(token);

        assertThat(userIdByToken).isEqualTo(userId);
    }
}
