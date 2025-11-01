package com.puspo.codearena.userservice.user.profile.dto;

import com.puspo.codearena.userservice.user.profile.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String avatarUrl;
    private String bio;
    private String githubLink;
    private String role;
    private Boolean isActive;
    private Integer problemsSolved;
    private Integer totalSubmissions;
    private Integer ranking;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .githubLink(user.getGithubLink())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .problemsSolved(user.getProblemsSolved())
                .totalSubmissions(user.getTotalSubmissions())
                .ranking(user.getRanking())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
