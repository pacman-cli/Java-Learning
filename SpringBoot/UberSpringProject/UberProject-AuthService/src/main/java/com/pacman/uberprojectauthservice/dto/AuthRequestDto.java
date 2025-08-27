package com.pacman.uberprojectauthservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    private String email;
    private String password;
}
