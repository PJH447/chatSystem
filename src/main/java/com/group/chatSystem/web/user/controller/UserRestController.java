package com.group.chatSystem.web.user.controller;

import com.group.chatSystem.web.common.dto.CommonResponse;
import com.group.chatSystem.web.user.dto.SignUpForm;
import com.group.chatSystem.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/signUp")
    public CommonResponse signUp(@RequestBody SignUpForm signUpForm) {
        userService.signUp(signUpForm);
        return CommonResponse.createVoidResponse();
    }

}
