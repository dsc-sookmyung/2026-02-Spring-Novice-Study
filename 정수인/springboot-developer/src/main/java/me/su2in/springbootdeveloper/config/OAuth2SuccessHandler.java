package me.su2in.springbootdeveloper.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.config.jwt.TokenProvider;
import me.su2in.springbootdeveloper.domain.RefreshToken;
import me.su2in.springbootdeveloper.domain.User;
import me.su2in.springbootdeveloper.repository.RefreshTokenRepository;
import me.su2in.springbootdeveloper.repository.UserRepository;
import me.su2in.springbootdeveloper.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14));

        RefreshToken token = refreshTokenRepository.findByUserId(user.getId())
                .map(entity -> entity.update(refreshToken))
                .orElseGet(() -> new RefreshToken(user.getId(), refreshToken));

        refreshTokenRepository.save(token);

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        CookieUtil.addCookie(response, "refresh_token", refreshToken, 60 * 60 * 24 * 14);

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        String targetUrl = UriComponentsBuilder.fromPath("/articles")
                .queryParam("token", accessToken)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
