package com.pacman.uberprojectauthservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String email;
    private Boolean success;
}
