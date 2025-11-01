package com.puspo.codearena.userservice.user.profile.repository;

import com.puspo.codearena.userservice.user.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    //find active users
    List<User> findByIsActiveTrue();

    //find users by role
    List<User> findByRole(User.UserRole role);

    //Search users by username (partial match)
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchByUsername(
            @Param("searchTerm") String searchTerm
    );

    User findUserById(UUID id);

    List<User> findTopUsersByProblemsSolved(Integer problemsSolved);
}
