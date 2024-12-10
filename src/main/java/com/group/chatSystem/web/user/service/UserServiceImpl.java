package com.group.chatSystem.web.user.service;

import com.group.chatSystem.web.user.domain.Role;
import com.group.chatSystem.web.user.domain.User;
import com.group.chatSystem.web.user.dto.SignUpForm;
import com.group.chatSystem.web.user.repository.RoleRepository;
import com.group.chatSystem.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
