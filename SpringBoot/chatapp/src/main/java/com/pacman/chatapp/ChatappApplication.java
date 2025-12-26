package com.pacman.chatapp;

import com.pacman.chatapp.model.ChatRoom;
import com.pacman.chatapp.repository.ChatRoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatappApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRooms(ChatRoomRepository roomRepo) {
        return args -> {
            // Check if any room with name "General" exists
            boolean exists = roomRepo.findAll().stream()
                    .anyMatch(room -> "General".equals(room.getName()));
            if (!exists) {
                ChatRoom defaultRoom = new ChatRoom();
                defaultRoom.setName("General"); // Let DB generate ID
                roomRepo.save(defaultRoom);
                System.out.println("Default chat room created: " + defaultRoom.getName());
            }
        };
    }


}
