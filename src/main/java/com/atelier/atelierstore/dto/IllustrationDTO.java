package com.atelier.atelierstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IllustrationDTO {
    private String id;
    private String name;
    private  String info;
    private  String imageUrl;
}
