package com.group.chatSystem.web.chat.service;

import com.group.chatSystem.web.chat.dto.ChatListDto;
import com.group.chatSystem.web.chat.dto.ChatMessageDto;
import com.group.chatSystem.web.chat.dto.CreateChatRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatService {

    ChatMessageDto createChat(Long targetUserId, CreateChatRequest createChatRequest);
    Slice<ChatMessageDto> getChatessageSlice(Long targetUserId, Pageable pageable);
    List<ChatListDto> findLastChatListByUser(Pageable pageable);
}
