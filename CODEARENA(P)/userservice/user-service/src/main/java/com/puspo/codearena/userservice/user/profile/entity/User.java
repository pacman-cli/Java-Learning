package com.puspo.codearena.userservice.user.profile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username", unique = true),
    @Index(name = "idx_email", columnList = "email", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  @Column(name = "username", unique = true, nullable = false, length = 50)
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "hashed_password", length = 255)
  private String hashedPassword;

  @Column(name = "oauth_provider", length = 50)
  private String oauthProvider; // 'github', 'google', 'local'

  @Column(name = "oauth_id", length = 255)
  private String oauthId;

  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  @Column(name = "bio", columnDefinition = "TEXT")
  private String bio;

  @Column(name = "github_link", length = 255)
  private String githubLink;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  @Builder.Default
  private UserRole role = UserRole.USER;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private Boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Statistics fields (optional - can be moved to separate table)
  @Column(name = "problems_solved")
  @Builder.Default
  private Integer problemsSolved = 0;

  @Column(name = "total_submissions")
  @Builder.Default
  private Integer totalSubmissions = 0;

  @Column(name = "ranking")
  private Integer ranking;

  // Enum for user roles
  public enum UserRole {
    USER,
    ADMIN,
    MODERATOR
  }
}
