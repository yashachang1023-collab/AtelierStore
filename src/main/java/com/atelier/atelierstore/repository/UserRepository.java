package com.atelier.atelierstore.repository;

import com.atelier.atelierstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    // Optional: useful if you want to check if a username is already taken during registration
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
