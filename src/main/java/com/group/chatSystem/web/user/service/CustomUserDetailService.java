package com.group.chatSystem.web.user.service;

import com.group.chatSystem.web.user.domain.AuthenticatedUser;
import com.group.chatSystem.web.user.domain.User;
import com.group.chatSystem.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        User user = userRepository.findByEmailAndEnabledIsTrue(email)
                                  .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user);

        HashMap<String, String> userinfo = new HashMap<>();
        userinfo.put("id", Optional.ofNullable(user.getId()).orElse(0L).toString());
        userinfo.put("email", user.getEmail());
        userinfo.put("nickname", user.getNickname());
        userinfo.put("phone", user.getPhone());

        request.getSession().setAttribute("userinfo", userinfo);

        return authenticatedUser;
    }
}
