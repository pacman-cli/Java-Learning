package com.pacman.chatapp.repository;

import com.pacman.chatapp.model.ChatMessage;
import com.pacman.chatapp.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
