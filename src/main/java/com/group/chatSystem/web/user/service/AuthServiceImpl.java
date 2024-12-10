package com.group.chatSystem.web.user.service;

import com.group.chatSystem.config.security.JwtTokenProvider;
import com.group.chatSystem.web.common.repository.CacheTokenRepository;
import com.group.chatSystem.web.user.domain.User;
import com.group.chatSystem.web.user.dto.LoginForm;
import com.group.chatSystem.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private static final String REFRESH_TOKEN_CACHE_PREFIX = "refresh::";
    private static final String REFRESH_TOKEN_COOKIE_KEY = "refresh";
    private static final String ACCESS_TOKEN_COOKIE_KEY = "access";

    @Value("${app.jwt.access-expiration-milliseconds}")
    private int jwtAccessExpirationDateMs;

    @Value("${app.jwt.refresh-expiration-milliseconds}")
    private long jwtRefreshExpirationDateMs;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CacheTokenRepository cacheTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void login(final LoginForm loginForm, final HttpServletResponse response) {
        User user = userRepository.findByEmailAndEnabledIsTrue(loginForm.email())
                                  .orElseThrow(RuntimeException::new);

        boolean matches = bCryptPasswordEncoder.matches(loginForm.password(), user.getPassword());
        if (!matches) {
            throw new RuntimeException();
        }

        this.issueToken(response, user);
    }

    private void issueToken(final HttpServletResponse response, final User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        this.setAccessToken(accessToken, response);

        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        this.setRefreshToken(refreshToken, response);
        cacheTokenRepository.setDataAndExpiration(REFRESH_TOKEN_CACHE_PREFIX + user.getId(), refreshToken, jwtRefreshExpirationDateMs);
    }

    private void setAccessToken(final String accessToken, final HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_KEY, accessToken)
                                                      .path("/")
                                                      .maxAge(jwtAccessExpirationDateMs)
                                                      .httpOnly(true)
                                                      .secure(true)
                                                      .sameSite("None")
                                                      .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private void setRefreshToken(final String refreshToken, final HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_KEY, refreshToken)
                                                      .path("/")
                                                      .maxAge(jwtRefreshExpirationDateMs)
                                                      .httpOnly(true)
                                                      .secure(true)
                                                      .sameSite("None")
                                                      .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
