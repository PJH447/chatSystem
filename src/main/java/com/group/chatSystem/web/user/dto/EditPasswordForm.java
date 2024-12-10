package com.group.chatSystem.web.user.dto;

public record EditPasswordForm(
        String password,
        String newPassword
) {
}
