package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.config.jwt.TokenAuthenticationFilter;
import org.example.config.jwt.TokenProvider;
import org.example.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import org.example.config.oauth.OAuth2SuccessHandler;
import org.example.config.oauth.OAuth2UserCustomService;
import org.example.repository.RefreshTokenRepository;
import org.example.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oauth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    // 1. 정적 리소스는 스프링 시큐리티 적용 제외
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/img/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**")
                );
    }

    // 2. 보안 필터 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))

                .addFilterBefore(
                        tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/token")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/**")
                        ).authenticated()
                        .anyRequest().permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(
                                        oauth2AuthorizationRequestBasedOnCookieRepository()
                                ))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2UserCustomService))
                        .successHandler(oAuth2SuccessHandler())
                )

                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) ->
                                        response.sendError(
                                                HttpStatus.UNAUTHORIZED.value(),
                                                HttpStatus.UNAUTHORIZED.getReasonPhrase()
                                        ),
                                new AntPathRequestMatcher("/api/**")
                        )
                )

                .build();
    }

    // 3. OAuth2 로그인 성공 처리
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(
                tokenProvider,
                refreshTokenRepository,
                oauth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    // 4. JWT 인증 필터
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    // 5. OAuth2 인증 요청 쿠키 저장소
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository
    oauth2AuthorizationRequestBasedOnCookieRepository() {

        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    // 6. 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}