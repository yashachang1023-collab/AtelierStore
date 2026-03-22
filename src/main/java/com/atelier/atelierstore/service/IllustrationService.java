package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.IllustrationDTO;

import java.util.List;

public interface IllustrationService {
    List<IllustrationDTO> getAllIllustrations();
    void addIllustration(IllustrationDTO dto);
    void deleteIllustration(Long id);
}
