package com.group.chatSystem.web.user.service;

import com.group.chatSystem.web.user.dto.LoginForm;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void login(LoginForm loginForm, HttpServletResponse response);
}