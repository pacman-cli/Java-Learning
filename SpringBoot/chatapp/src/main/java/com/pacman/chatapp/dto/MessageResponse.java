package com.pacman.chatapp.dto;


import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse{
        private Long id;
        private Long roomId;
        private String sender;
        private String content;
}
