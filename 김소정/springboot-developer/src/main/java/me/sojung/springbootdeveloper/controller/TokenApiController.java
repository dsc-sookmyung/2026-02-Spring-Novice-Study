package me.sojung.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.sojung.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.sojung.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.sojung.springbootdeveloper.service.TokenService;
import me.sojung.springbootdeveloper.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import me.sojung.springbootdeveloper.service.RefreshTokenService;
import me.sojung.springbootdeveloper.util.CookieUtil;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

    @DeleteMapping("/api/refresh-token")
    public ResponseEntity<Void> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        // 1. 인증 정보에서 사용자의 이메일 확인
        String email = authentication.getName();
        Long userId = userService.findByEmail(email).getId();

        // 2. 해당 사용자의 DB 리프레시 토큰 삭제
        refreshTokenService.deleteByUserId(userId);

        // 3. refresh_token 쿠키 만료
        CookieUtil.deleteCookie(request, response, "refresh_token");

        // 4. HTTP 204 반환
        return ResponseEntity.noContent().build();
    }
}