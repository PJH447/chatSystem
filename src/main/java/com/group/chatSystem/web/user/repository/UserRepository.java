package com.group.chatSystem.web.user.repository;

import com.group.chatSystem.web.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
