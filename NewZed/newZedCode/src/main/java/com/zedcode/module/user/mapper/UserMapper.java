package com.zedcode.module.user.mapper;

import com.zedcode.module.user.dto.CreateUserRequest;
import com.zedcode.module.user.dto.UpdateUserRequest;
import com.zedcode.module.user.dto.UserDTO;
import com.zedcode.module.user.entity.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MapStruct mapper for User entity and DTOs
 * Handles conversions between User, UserDTO, CreateUserRequest, and UpdateUserRequest
 *
 * @author ZedCode
 * @version 1.0
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    /**
     * Convert User entity to UserDTO
     *
     * @param user the User entity
     * @return UserDTO
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserDTO toDTO(User user);

    /**
     * Convert list of User entities to list of UserDTOs
     *
     * @param users list of User entities
     * @return list of UserDTOs
     */
    List<UserDTO> toDTOList(List<User> users);

    /**
     * Convert Page of User entities to Page of UserDTOs
     *
     * @param userPage Page of User entities
     * @return Page of UserDTOs
     */
    default Page<UserDTO> toDTOPage(Page<User> userPage) {
        return userPage.map(this::toDTO);
    }

    /**
     * Convert CreateUserRequest to User entity
     * Password will be encoded separately in the service layer
     *
     * @param request CreateUserRequest
     * @return User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Password will be encoded separately
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "failedLoginAttempts", constant = "0")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    User toEntity(CreateUserRequest request);

    /**
     * Update User entity from UpdateUserRequest
     * Only updates non-null fields from the request
     *
     * @param request      UpdateUserRequest
     * @param existingUser the existing User entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true) // Username cannot be changed
    @Mapping(target = "password", ignore = true) // Password is updated separately
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    void updateEntityFromDTO(UpdateUserRequest request, @MappingTarget User existingUser);

    /**
     * Convert UserDTO to User entity (for partial updates)
     *
     * @param userDTO the UserDTO
     * @return User entity
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserDTO userDTO);

    /**
     * Custom method to safely convert string to UserStatus
     *
     * @param status status string
     * @return UserStatus enum
     */
    default User.UserStatus mapStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }
        try {
            return User.UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Custom method to safely convert string to UserRole
     *
     * @param role role string
     * @return UserRole enum
     */
    default User.UserRole mapRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }
        try {
            return User.UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * After mapping hook - can be used for custom post-processing
     *
     * @param user the mapped User entity
     */
    @AfterMapping
    default void afterMapping(@MappingTarget User user) {
        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(User.UserRole.USER);
        }

        // Set default status if not provided
        if (user.getStatus() == null) {
            user.setStatus(User.UserStatus.PENDING);
        }

        // Ensure deleted flag is not null
        if (user.getDeleted() == null) {
            user.setDeleted(false);
        }
    }
}
