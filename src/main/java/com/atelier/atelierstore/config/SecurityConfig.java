package com.atelier.atelierstore.config;

import com.atelier.atelierstore.exception.CustomAccessDeniedHandler;
import com.atelier.atelierstore.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Security rules evaluated sequentially from top to bottom
                .authorizeHttpRequests(auth -> auth
                        //Allow all users to browse the gallery and stationery.
                        .requestMatchers(HttpMethod.GET, "/api/gallery/**", "/api/stationery/**").permitAll()
                        //Allow public access to authentication endpoints (login/register)
                        .requestMatchers("/api/auth/**").permitAll()
                        //Administrative actions: Restrict management operations (POST, PUT, DELETE) to ADMIN role
                        // Since GET is already permitted above, these lines catch all other HTTP methods
                        .requestMatchers("/api/gallery/**").hasRole("ADMIN")
                        .requestMatchers("/api/stationery/**").hasRole("ADMIN")
                        //Customer actions: Ensure users are authenticated for personal features like cart and orders
                        .requestMatchers("/api/cart/**", "/api/orders/**").authenticated()
                        //Default: Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint) //401
                        .accessDeniedHandler(customAccessDeniedHandler) //403
                );
        return http.build();
    }
}

