package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.mapper.IllustrationMapper;
import com.atelier.atelierstore.model.Illustration;
import com.atelier.atelierstore.repository.IllustrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IllustrationServiceImpl implements IllustrationService{

    private final IllustrationRepository illustrationRepository;
    private final IllustrationMapper illustrationMapper;
    private final FileStorageService fileStorageService;
    //展示所有插画
    @Override
    public List<IllustrationDTO> getAllIllustrations(){
        log.info(">>>> [业务开始] 正在从数据库获取所有插画作品...");
        List<IllustrationDTO> illustrationDTOS = illustrationRepository.findAll().stream()
                .map(illustrationMapper::toDto).collect(Collectors.toList());
        log.info(">>>> [业务结束] 成功转化了 {} 件插画作品。", illustrationDTOS.size());
        return illustrationDTOS;
    }

    //新增插画
    @Override
    public  void addIllustration(IllustrationDTO illustrationDTO){
        Illustration illustration= illustrationMapper.toEntity(illustrationDTO);
        illustrationRepository.save(illustration);
    }


    /**
     * Deletes an illustration from the database and its physical file from storage.
     * @param id the ID of the illustration to delete
     */
    @Override
    public void deleteIllustration(Long id) {
        // 1. Find the illustration first to get the image filename
        Illustration illustration = illustrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork not found with ID: " + id));

        // 2. Delete the record from the database
        illustrationRepository.deleteById(id);

        // 3. Delete the physical file from the /uploads folder
        // illustration.getImageUrl() contains the UUID filename (e.g., "7dc3aa...jpg")
        fileStorageService.deleteFile(illustration.getImageUrl());
    }
}
