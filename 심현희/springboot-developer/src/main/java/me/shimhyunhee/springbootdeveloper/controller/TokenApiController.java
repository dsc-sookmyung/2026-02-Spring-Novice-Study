package me.shimhyunhee.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.shimhyunhee.springbootdeveloper.config.oauth.OAuth2SuccessHandler;
import me.shimhyunhee.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.shimhyunhee.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.shimhyunhee.springbootdeveloper.service.RefreshTokenService;
import me.shimhyunhee.springbootdeveloper.service.TokenService;
import me.shimhyunhee.springbootdeveloper.util.CookieUtil;
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
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken
            (@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

    // 리프레시 토큰을 삭제하는 API
    @DeleteMapping("/api/refresh-token")
    public ResponseEntity deleteRefreshToken(HttpServletRequest request,
                                             HttpServletResponse response) {

        refreshTokenService.delete(); // DB의 리프레시 토큰 삭제
        CookieUtil.deleteCookie(request, response, OAuth2SuccessHandler.REFRESH_TOKEN_COOKIE_NAME); // 브라우저 쿠키의 리프레시 토큰 삭제

        return ResponseEntity.ok()
                .build();
    }
}
