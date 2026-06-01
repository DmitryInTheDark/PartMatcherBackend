package ru.app.partmatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.app.partmatcher.entity.SupportChatMessage;
import ru.app.partmatcher.entity.User;

import java.util.List;

public interface SupportChatMessageRepository extends JpaRepository<SupportChatMessage, Long> {

    @Query("select m from SupportChatMessage m where (m.sender.id = :userId and m.recipient.id = :counterpartyId) or (m.sender.id = :counterpartyId and m.recipient.id = :userId) order by m.sentAt asc")
    List<SupportChatMessage> findConversation(@Param("userId") Long userId, @Param("counterpartyId") Long counterpartyId);

    @Query(value = "SELECT DISTINCT u.* FROM users u WHERE u.id IN (" +
            "SELECT DISTINCT recipient_id FROM support_chat_messages WHERE sender_id = :userId " +
            "UNION " +
            "SELECT DISTINCT sender_id FROM support_chat_messages WHERE recipient_id = :userId)", nativeQuery = true)
    List<User> findChatContacts(@Param("userId") Long userId);
}
