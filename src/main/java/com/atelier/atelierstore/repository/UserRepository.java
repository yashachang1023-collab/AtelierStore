package com.atelier.atelierstore.repository;

import com.atelier.atelierstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
