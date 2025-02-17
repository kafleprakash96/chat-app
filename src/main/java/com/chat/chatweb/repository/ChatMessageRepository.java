package com.chat.chatweb.repository;

import com.chat.chatweb.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findByChatRoomIdOrderByTimestampDesc (Long roomId);
}
