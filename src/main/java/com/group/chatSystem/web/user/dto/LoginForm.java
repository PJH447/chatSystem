package com.group.chatSystem.web.user.dto;

public record LoginForm(
        String email,
        String password
) {
}