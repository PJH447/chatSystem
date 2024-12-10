package com.group.chatSystem.web.chat.controller;


import com.group.chatSystem.web.chat.dto.ChatMessageDto;
import com.group.chatSystem.web.chat.dto.CreateChatRequest;
import com.group.chatSystem.web.chat.service.ChatService;
import com.group.chatSystem.web.common.dto.CommonResponse;
import com.group.chatSystem.web.user.domain.AuthenticatedUser;
import com.group.chatSystem.web.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

import static com.group.chatSystem.web.common.dto.CommonResponse.createResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRestController {

    private final AuthService authService;
    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/socket")
    public CommonResponse createAuthorizationHeader(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.setAuthorizationHeader(request, response);
        return CommonResponse.createVoidResponse();
    }

    @MessageMapping("talk.{targetUserId}")
    public void chat(@DestinationVariable Long targetUserId, CreateChatRequest createChatRequest) {
        log.info("chat");
        ChatMessageDto chat = chatService.createChat(targetUserId, createChatRequest);

        rabbitTemplate.convertAndSend(exchangeName, "*.user." + targetUserId, chat);
    }

    @MessageMapping("enter.{targetUserId}")
    public void enter(@DestinationVariable Long targetUserId) {
        log.info("enter");
        ChatMessageDto enter = ChatMessageDto.builder()
                                             .message("enter")
                                             .isNotice(true)
                                             .build();

        rabbitTemplate.convertAndSend(exchangeName, "enter.user." + targetUserId, enter);
    }

    @MessageMapping("exit.{targetUserId}")
    public void exit(@DestinationVariable Long targetUserId) {
        log.info("exit");
        ChatMessageDto exit = ChatMessageDto.builder()
                                            .message("exit")
                                            .isNotice(true)
                                            .build();

        rabbitTemplate.convertAndSend(exchangeName, "exit.user." + targetUserId, exit);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/chat")
    public CommonResponse getChatMessageSlice(
            @RequestParam Long targetUserId,
            @AuthenticationPrincipal AuthenticatedUser user,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Set<String> roles = user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());
        boolean isAdmin = roles.contains("ADMIN");

        boolean isOwnRequest = targetUserId.equals(user.getId());

        if (!isOwnRequest && !isAdmin) {
            throw new RuntimeException("권한이 없음");
        }
        return createResponse(chatService.getChatessageSlice(targetUserId, pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/chat/list")
    public CommonResponse getChatUserList(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return createResponse(chatService.findLastChatListByUser(pageable));
    }
}
