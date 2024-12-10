package com.group.chatSystem.web.user.service;

import com.group.chatSystem.exception.BadRequestException;
import com.group.chatSystem.exception.DuplicateException;
import com.group.chatSystem.exception.ResultNotFoundException;
import com.group.chatSystem.web.user.domain.Role;
import com.group.chatSystem.web.user.domain.User;
import com.group.chatSystem.web.user.dto.EditInfoForm;
import com.group.chatSystem.web.user.dto.EditPasswordForm;
import com.group.chatSystem.web.user.dto.SignUpForm;
import com.group.chatSystem.web.user.repository.RoleRepository;
import com.group.chatSystem.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void signUp(final SignUpForm signUpForm) {
        this.validateDuplicateEmail(signUpForm.email());
        this.validateDuplicateNickname(signUpForm.nickname());
        this.validateDuplicatePhone(signUpForm.phone());
        User user = User.builder()
                        .email(signUpForm.email())
                        .phone(signUpForm.phone())
                        .name(signUpForm.name())
                        .nickname(signUpForm.nickname())
                        .password(bCryptPasswordEncoder.encode(signUpForm.password()))
                        .build();

        Role role = roleRepository.findByAuthority("USER");
        user.initRole(role);

        User _user = userRepository.save(user);
    }

    @Override
    public boolean existSameNickname(final String nickname) {
        return userRepository.findByNicknameAndEnabledIsTrue(nickname).isPresent();
    }

    @Override
    public boolean existSameEmail(final String email) {
        return userRepository.findByEmailAndEnabledIsTrue(email).isPresent();
    }

    @Transactional
    @Override
    public void editNickname(final Long userId, final EditInfoForm editInfoForm) {
        this.validateDuplicateNickname(editInfoForm.nickname());
        this.validateDuplicatePhone(editInfoForm.phone());

        User user = getUser(userId);
        user.editNickname(editInfoForm.nickname());
        user.editPhone(editInfoForm.phone());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void editPassword(final Long userId, final EditPasswordForm editPasswordForm) {
        User user = getUser(userId);
        String password = user.getPassword();
        boolean matches = bCryptPasswordEncoder.matches(editPasswordForm.password(), password);

        if (!matches) {
            throw new BadRequestException("비밀번호가 틀렸습니다.");
        }

        user.editPassword(bCryptPasswordEncoder.encode(editPasswordForm.newPassword()));

        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(()-> new ResultNotFoundException("user 가 존재하지 않습니다."));
    }
    private void validateDuplicateEmail(final String email) {
        if (this.existSameEmail(email)) {
            throw new DuplicateException("동일 email 유저가 존재합니다.");
        }
    }

    private void validateDuplicateNickname(final String nickname) {
        if (this.existSameNickname(nickname)) {
            throw new DuplicateException("동일 닉네임 유저가 존재합니다.");
        }
    }

    private void validateDuplicatePhone(final String phone) {
        Optional<User> userByPhone = userRepository.findByPhoneAndEnabledIsTrue(phone);
        if (userByPhone.isPresent()) {
            throw new DuplicateException("동일 전화번호 유저가 존재합니다.");
        }
    }

}
