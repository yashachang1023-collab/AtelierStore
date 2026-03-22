package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.AuthResponse;
import com.atelier.atelierstore.dto.LoginRequest;
import com.atelier.atelierstore.model.User;
import com.atelier.atelierstore.repository.UserRepository;
import com.atelier.atelierstore.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public AuthResponse login(LoginRequest request){
        //1 find user in database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        //2 Verify password(Plain text from request vs BCrypt hash from DB)
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        //3 if success,generate the token
        String token = jwtUtils.generateToken(user);

        //4 return the token and essential info
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
