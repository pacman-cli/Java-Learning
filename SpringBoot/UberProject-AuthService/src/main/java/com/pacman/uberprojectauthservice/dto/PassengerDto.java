package com.pacman.uberprojectauthservice.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password; //encrypted password
    private Date createdAt;
}
