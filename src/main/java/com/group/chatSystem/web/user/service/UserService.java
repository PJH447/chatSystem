package com.group.chatSystem.web.user.service;

import com.group.chatSystem.web.user.dto.EditInfoForm;
import com.group.chatSystem.web.user.dto.EditPasswordForm;
import com.group.chatSystem.web.user.dto.SignUpForm;

public interface UserService {

    void signUp(SignUpForm signUpForm);
    boolean existSameNickname(String nickname);
    boolean existSameEmail(String email);
    void editNickname(Long userId, EditInfoForm editInfoForm);
    void editPassword(Long userId, EditPasswordForm editPasswordForm);
}
