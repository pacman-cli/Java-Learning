package com.pacman.uberreviewservice.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
    private Long id;
    private String name;
    private String licenceNumber;
    private String rating;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
