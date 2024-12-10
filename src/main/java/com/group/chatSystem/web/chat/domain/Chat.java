package com.group.chatSystem.web.chat.domain;

import com.group.chatSystem.web.common.domain.BaseEntity;
import com.group.chatSystem.web.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "chat", indexes = {})
public class Chat extends BaseEntity {

    @Id
    @Column(name = "chat_id", columnDefinition = "bigint(20)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

}
