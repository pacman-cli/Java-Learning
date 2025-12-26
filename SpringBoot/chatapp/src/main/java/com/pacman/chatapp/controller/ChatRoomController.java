package com.pacman.chatapp.controller;

import com.pacman.chatapp.model.ChatRoom;
import com.pacman.chatapp.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping
    public List<ChatRoom> getAllRooms(){
        return chatRoomRepository.findAll();
    }

    @PostMapping
    public ChatRoom createRoom(@RequestBody ChatRoom room){
        return chatRoomRepository.save(room);
    }
}
