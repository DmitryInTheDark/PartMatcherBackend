package ru.app.partmatcher.mapper;

import ru.app.partmatcher.dto.ChatMessageDto;
import ru.app.partmatcher.entity.SupportChatMessage;

public class ChatMessageMapper {

    public static ChatMessageDto toDto(SupportChatMessage entity) {
        if (entity == null) {
            return null;
        }
        return ChatMessageDto.builder()
                .id(entity.getId())
                .senderId(entity.getSender().getId())
                .recipientId(entity.getRecipient().getId())
                .content(entity.getContent())
                .sentAt(entity.getSentAt())
                .read(entity.isRead())
                .build();
    }
}
