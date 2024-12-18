package com.group.chatSystem.web.user.controller;

import com.group.chatSystem.web.common.dto.CommonResponse;
import com.group.chatSystem.web.user.dto.LoginForm;
import com.group.chatSystem.web.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.group.chatSystem.web.common.dto.CommonResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/login")
    public CommonResponse login(
            @RequestBody LoginForm loginForm,
            HttpServletResponse response
    ) {
        authService.login(loginForm, response);
        return createVoidResponse();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/reissue")
    public CommonResponse reissueAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.reissueAccessToken(request, response);
        return CommonResponse.createVoidResponse();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/logout")
    public CommonResponse logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return CommonResponse.createVoidResponse();
    }

}
