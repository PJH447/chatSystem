package com.group.chatSystem.web.user.service;

import com.group.chatSystem.web.user.dto.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void login(LoginForm loginForm, HttpServletResponse response);
    void reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
    void logout(final HttpServletRequest request, final HttpServletResponse response);
}
