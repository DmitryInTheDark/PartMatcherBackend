package ru.app.partmatcher.websocket;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.app.partmatcher.service.CustomUserDetailsService;
import ru.app.partmatcher.service.JwtService;

@Component
@RequiredArgsConstructor
public class WebSocketJwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null) {
                authHeader = accessor.getFirstNativeHeader("authToken");
            }
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Требуется JWT-авторизация для WebSocket");
            }
            String token = authHeader.substring(7);
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                accessor.setUser(authentication);
            } else {
                throw new IllegalArgumentException("Неверный JWT для WebSocket подключения");
            }
        }
        return message;
    }
}
