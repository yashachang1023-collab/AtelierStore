package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.AuthResponse;
import com.atelier.atelierstore.dto.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
