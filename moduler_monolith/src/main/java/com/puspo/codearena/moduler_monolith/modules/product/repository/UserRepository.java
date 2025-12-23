package com.puspo.codearena.moduler_monolith.modules.product.repository;

import com.puspo.codearena.moduler_monolith.modules.product.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
