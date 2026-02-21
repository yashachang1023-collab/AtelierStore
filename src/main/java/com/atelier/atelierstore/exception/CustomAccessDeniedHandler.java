package com.atelier.atelierstore.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    //It's triggered when the access was denied
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Set the response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //Set HTTP status code to 403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        //create a custom error body
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_FORBIDDEN); // 403
        body.put("error", "Forbidden");
        body.put("message", "Access Denied: Admin role required.");
        body.put("path", request.getServletPath());
        body.put("timestamp", LocalDateTime.now().toString());

        //Convert the map to JSON and output it
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
