package com.atelier.atelierstore.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*This class handles 401 Unauthorized errors
It's triggered when a user tries to access a protected resource without authentication.
*/
 @Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //Set the response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //Set the HTTP status code to 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //create a custom error response body
        final Map<String, Object> boby = new HashMap<>();
        boby.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        boby.put("error", "Unauthorized");
        boby.put("message", authException.getMessage());
        boby.put("path", request.getServletPath());
        boby.put("timestamp", LocalDateTime.now().toString());

        //Convert the map to JSON and output it
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),boby);

    }
}
