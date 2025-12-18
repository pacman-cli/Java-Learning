package com.puspo.scalablekafkaapp.userservice.infrastructure.persistence;

import com.puspo.scalablekafkaapp.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
