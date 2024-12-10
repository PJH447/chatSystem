package com.group.chatSystem.web.chat.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageDto(
        Long chatId,
        String message,
        Long senderId,
        String senderNickname,
        LocalDateTime createdAt,
        Boolean isNotice
) {

}
