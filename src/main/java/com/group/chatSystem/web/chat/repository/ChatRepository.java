package com.group.chatSystem.web.chat.repository;


import com.group.chatSystem.web.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatDslRepository {
}
