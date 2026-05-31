package ru.app.partmatcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.app.partmatcher.dto.ChatMessageDto;
import ru.app.partmatcher.service.ChatService;
import ru.app.partmatcher.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/support/chat")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPPORT','ADMIN','USER')")
@Tag(name = "Support", description = "Чат поддержки")
public class SupportChatController {

    private final ChatService chatService;
    private final UserService userService;

    @Operation(summary = "Получить историю переписки с другим пользователем")
    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDto>> getConversation(@RequestParam Long counterpartyId) {
        Long currentUserId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(chatService.getConversation(currentUserId, counterpartyId));
    }
}
