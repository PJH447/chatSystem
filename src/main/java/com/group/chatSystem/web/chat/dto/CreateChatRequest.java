package com.group.chatSystem.web.chat.dto;

public record CreateChatRequest(
        String senderEmail,
        String message
) {
}
