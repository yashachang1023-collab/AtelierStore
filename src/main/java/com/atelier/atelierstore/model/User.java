package com.atelier.atelierstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String email;
    // The display name shown in the UI (can also be unique)
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    // Use a String or Enum for roles: e.g., "ROLE_ADMIN", "ROLE_CUSTOMER"
    private String role;
}
