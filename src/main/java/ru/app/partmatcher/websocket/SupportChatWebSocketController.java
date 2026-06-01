package ru.app.partmatcher.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import ru.app.partmatcher.dto.ChatMessageDto;
import ru.app.partmatcher.entity.User;
import ru.app.partmatcher.service.ChatService;
import ru.app.partmatcher.service.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SupportChatWebSocketController {

    private final ChatService chatService;
    private final UserService userService;

    @MessageMapping("/support/message")
    @SendTo("/topic/support")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto message, @AuthenticationPrincipal Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Требуется авторизация WebSocket");
        }
        User sender = userService.getByEmail(principal.getName());
        return chatService.saveMessage(sender.getId(), message.getRecipientId(), message.getContent());
    }
}
