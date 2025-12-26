package com.pacman.taskmanagementsystemadvancedversion.repository;

import com.pacman.taskmanagementsystemadvancedversion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
