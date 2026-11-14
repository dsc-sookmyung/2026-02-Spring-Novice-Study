package me.yeonah.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.yeonah.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.yeonah.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.yeonah.springbootdeveloper.service.RefreshTokenService;
import me.yeonah.springbootdeveloper.service.TokenService;
import me.yeonah.springbootdeveloper.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

    //로그아웃 시 리프레시 토큰(DB, 브라우저) 삭제
    @DeleteMapping("/api/refresh-token")
    public ResponseEntity<Void> deleteRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.delete();
        CookieUtil.deleteCookie(request, response, "refresh_token");

        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}