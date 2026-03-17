package com.atelier.atelierstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IllustrationUploadDTO {
    @NotBlank(message = "Title is required")
    private String name;
    private String info;
}