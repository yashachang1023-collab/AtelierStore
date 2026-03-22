package com.atelier.atelierstore.controller;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.mapper.IllustrationMapper;
import com.atelier.atelierstore.model.Illustration;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.IllustrationRepository;
import com.atelier.atelierstore.service.FileStorageService;
import com.atelier.atelierstore.service.IllustrationService;
import com.atelier.atelierstore.service.StationeryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final IllustrationService illustrationService;
    private final StationeryService stationeryService;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private IllustrationRepository illustrationRepository;

    @Autowired
    private IllustrationMapper illustrationMapper;

   @GetMapping("/gallery")
   public List<IllustrationDTO> getGallery(){
       return illustrationService.getAllIllustrations();
   }


   //add illustrations
   @PostMapping("/gallery")
   public String addIllustration(@Valid @RequestBody IllustrationDTO illustrationDTO){
       illustrationService.addIllustration(illustrationDTO);
       return "插画保存成功！ ID 是：" + illustrationDTO.getId();

   }

    @PostMapping("/gallery/upload")
    public ResponseEntity<IllustrationDTO> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("name") String name,
                                              @RequestParam("info") String info) {
        // 1. Store the file and get the unique name
        String fileName = fileStorageService.storeFile(file);

        // 2. Create and save the Database Entity
        Illustration illustration = new Illustration();
        illustration.setName(name);
        illustration.setInfo(info);
        // We store the filename in the database (frontend will prefix it with /uploads/)
        illustration.setImageUrl(fileName);

        Illustration savedItem = illustrationRepository.save(illustration);

        // 3. Return the DTO of the saved artwork
        return ResponseEntity.ok(illustrationMapper.toDto(savedItem));

    }

    @DeleteMapping("/gallery/{id}")
/**
 * API endpoint to remove an illustration.
 * Only accessible by ADMIN (configured in SecurityConfig).
 */
    public ResponseEntity<String> deleteIllustration(@PathVariable Long id) {
        illustrationService.deleteIllustration(id);
        return ResponseEntity.ok("Illustration with ID " + id + " has been successfully removed.");
    }

    //购买文具
    @PutMapping("/shop/buy/{id}")
    public String buyStationery(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer num) throws OutOfStockException {

            stationeryService.buyStationery(id,num);
            return "购买成功！";
    }

    //按照类别搜索文具
    @GetMapping("/shop/search")
    public List<Stationery> searchStationeryByCategory(@RequestParam("category") String category){
       return stationeryService.getStationeryByCategory(category);
    }
}

