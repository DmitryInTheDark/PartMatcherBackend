package ru.app.partmatcher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.ChatMessageDto;
import ru.app.partmatcher.entity.SupportChatMessage;
import ru.app.partmatcher.entity.User;
import ru.app.partmatcher.exception.ResourceNotFoundException;
import ru.app.partmatcher.mapper.ChatMessageMapper;
import ru.app.partmatcher.repository.SupportChatMessageRepository;
import ru.app.partmatcher.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SupportChatMessageRepository repository;
    private final UserRepository userRepository;

    public ChatMessageDto saveMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Отправитель не найден"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Получатель не найден"));

        SupportChatMessage message = SupportChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();
        return ChatMessageMapper.toDto(repository.save(message));
    }

    public List<ChatMessageDto> getConversation(Long userId, Long counterpartyId) {
        return repository.findConversation(userId, counterpartyId).stream()
                .map(ChatMessageMapper::toDto)
                .collect(Collectors.toList());
    }
}
