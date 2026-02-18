package com.atelier.atelierstore.controller;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.model.Illustration;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private InventoryService inventoryService;

   @GetMapping("/gallery")
   public List<IllustrationDTO> getGallery(){
       return inventoryService.getAllIllustration();
   }


   //add illustrations
   @PostMapping("/gallery")
   public String addIllustration(@Valid @RequestBody IllustrationDTO illustrationDTO){
       inventoryService.addIllustration(illustrationDTO);
       return "插画保存成功！ ID 是：" + illustrationDTO.getId();

   }

    //删除插画
   @DeleteMapping("/gallery/{id}")
    public String deleteIllutration(@PathVariable String id){
        inventoryService.deleteIllustration(id);
       return "插画 ID: " + id + " 已从画廊移除。";
    }

    //购买文具
    @PutMapping("/shop/buy/{id}")
    public String buyStationery(@PathVariable String id, @RequestParam(defaultValue = "1") Integer num) throws OutOfStockException {

            inventoryService.buyStationery(id,num);
            return "购买成功！";
    }

    //按照类别搜索文具
    @GetMapping("/shop/search")
    public List<Stationery> searchStationeryByCategory(@RequestParam("category") String category){
       return inventoryService.getStationeryByCategory(category);
    }
}

