package com.group.chatSystem.web.user.repository;

import com.group.chatSystem.web.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndEnabledIsTrue(String email);

    Optional<User> findByNicknameAndEnabledIsTrue(String nickname);
}
