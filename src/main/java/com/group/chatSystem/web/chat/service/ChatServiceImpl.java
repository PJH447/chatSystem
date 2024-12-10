package com.group.chatSystem.web.chat.service;

import com.group.chatSystem.web.chat.domain.Chat;
import com.group.chatSystem.web.chat.dto.ChatListDto;
import com.group.chatSystem.web.chat.dto.ChatMessageDto;
import com.group.chatSystem.web.chat.dto.CreateChatRequest;
import com.group.chatSystem.web.chat.repository.ChatRepository;
import com.group.chatSystem.web.user.domain.User;
import com.group.chatSystem.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ChatMessageDto createChat(final Long targetUserId, final CreateChatRequest createChatRequest) {
        User sender = userRepository.findByEmailAndEnabledIsTrue(createChatRequest.senderEmail())
                                    .orElseThrow(RuntimeException::new);
        User targetUser = userRepository.findById(targetUserId)
                                  .orElseThrow(RuntimeException::new);

        Chat chat = Chat.builder()
                        .user(sender)
                        .message(createChatRequest.message())
                        .targetUser(targetUser)
                        .build();

        chat.validateEmptyMessage();

        Chat _chat = chatRepository.save(chat);

        return ChatMessageDto.builder()
                             .chatId(_chat.getId())
                             .senderId(chat.getUser().getId())
                             .senderNickname(sender.getNickname())
                             .message(_chat.getMessage())
                             .createdAt(_chat.getCreatedAt())
                             .isNotice(false)
                             .build();
    }

    @Override
    public Slice<ChatMessageDto> getChatessageSlice(final Long targetUserId, final Pageable pageable) {

        Slice<Chat> recentChat = chatRepository.findRecentChat(targetUserId, pageable);

        List<ChatMessageDto> list = recentChat.stream()
                                              .map(chat -> ChatMessageDto.builder()
                                                                         .chatId(chat.getId())
                                                                         .senderId(chat.getUser().getId())
                                                                         .senderNickname(chat.getUser().getNickname())
                                                                         .message(chat.getMessage())
                                                                         .createdAt(chat.getCreatedAt())
                                                                         .isNotice(false)
                                                                         .build()
                                              )
                                              .collect(Collectors.toList());
        Collections.reverse(list);

        return new SliceImpl<>(list, recentChat.getPageable(), recentChat.hasNext());
    }

    @Override
    public List<ChatListDto> findLastChatListByUser(final Pageable pageable) {
        List<Chat> chatList = chatRepository.findLastChatListByUser(pageable);

        return chatList.stream()
                       .map(chat -> {
                           User user = chat.getTargetUser();
                           return ChatListDto.builder()
                                             .chatId(chat.getId())
                                             .userId(user.getId())
                                             .userNickname(user.getNickname())
                                             .message(chat.getMessage())
                                             .build();

                       }).collect(Collectors.toList());

    }
}
