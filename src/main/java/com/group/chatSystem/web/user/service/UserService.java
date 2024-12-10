package com.group.chatSystem.web.user.service;

import com.group.chatSystem.web.user.dto.SignUpForm;

public interface UserService {

    void signUp(SignUpForm signUpForm);
    boolean existSameNickname(String nickname);
    boolean existSameEmail(String email);
}
