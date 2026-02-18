package com.atelier.atelierstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IllustrationDTO {
    private String id;

    @NotBlank(message = "Name is required")
    private String name;
    @Size(max = 255, message = "Description must be under 255 characters")
    private  String info;
    @NotBlank(message = "imageUrl is required")
    private  String imageUrl;
}
