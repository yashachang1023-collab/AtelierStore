package com.atelier.atelierstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory.", ex);
        }
    }

    /**
     * Stores a file on the disk with a unique name.
     * @param file the multipart file from the controller
     * @return the unique file name
     */
    public String storeFile(MultipartFile file) {
        // 1. Validate File Type (MIME Type check)
        // We only allow common image formats
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/gif"))) {
            throw new RuntimeException("Invalid file type! Only JPG, PNG and GIF are allowed.");
        }

        // 2. Validate File Name (Same as before)
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }


    /**
     * Deletes a file from the physical storage.
     * Used when an illustration is removed from the database.
     * @param fileName the unique UUID filename stored in DB
     */
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
            System.out.println(">> [FileService] Successfully deleted file: " + fileName);
        } catch (IOException ex) {
            // We log the error but don't necessarily want to crash the whole app
            System.err.println(">> [FileService] Failed to delete file: " + fileName);
        }
    }
}