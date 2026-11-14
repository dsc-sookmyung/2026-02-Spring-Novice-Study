package me.yuninyeong.springbootdeveloper.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.yuninyeong.springbootdeveloper.dto.AddUserRequest;
import me.yuninyeong.springbootdeveloper.service.RefreshTokenService;
import me.yuninyeong.springbootdeveloper.service.UserService;
import me.yuninyeong.springbootdeveloper.util.CookieUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.WebUtils;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request); // 회원가입 메서드 호출
        return "redirect:/login"; // 회원가입이 완료된 이후에 로그인 페이지로 이동
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        Cookie refreshTokenCookie =
                WebUtils.getCookie(request, "refresh_token");

        if (refreshTokenCookie != null) {
            refreshTokenService.deleteByRefreshToken(
                    refreshTokenCookie.getValue()
            );
        }

        CookieUtil.deleteCookie(request, response, "refresh_token");

        new SecurityContextLogoutHandler().logout(
                request,
                response,
                SecurityContextHolder.getContext().getAuthentication()
        );

        return ResponseEntity.noContent().build();
    }
}
