package com.pacman.chatapp.controller;

import com.pacman.chatapp.dto.MessageRequest;
import com.pacman.chatapp.model.ChatMessage;
import com.pacman.chatapp.model.ChatRoom;
import com.pacman.chatapp.repository.ChatMessageRepository;
import com.pacman.chatapp.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@MessageMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/sendMessage")
    public void sendMessage(MessageRequest messageRequest) {
        // 1️⃣ Get the chat room by ID
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // 2️⃣ Create ChatMessage entity
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(messageRequest.get());
        chatMessage.setContent(messageRequest.getContent());
        chatMessageRepository.save(chatMessage);

        // 3️⃣ Send message to subscribers
        MessageResponse response = new MessageResponse(
                chatMessage.getId(),
                chatRoom.getId(),
                chatMessage.getSender(),
                chatMessage.getContent()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), response);
    }
}
