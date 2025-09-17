package com.pacman.chatapp.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private Long roomId;
    private String sender;
    private String content;

    // getters and setters
}
