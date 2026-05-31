package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String content;
    private LocalDateTime sentAt;
    private boolean read;
}
