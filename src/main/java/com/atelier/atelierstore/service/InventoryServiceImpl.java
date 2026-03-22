package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.mapper.IllustrationMapper;
import com.atelier.atelierstore.model.Illustration;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.IllustrationRepository;
import com.atelier.atelierstore.repository.StationeryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private IllustrationRepository illustrationRepository;
    @Autowired
    private StationeryRepository stationeryRepository;
    @Autowired
    private IllustrationMapper illustrationMapper;

    @Autowired
    private FileStorageService fileStorageService;

    //展示所有插画
    @Override
    public List<IllustrationDTO> getAllIllustration(){
        log.info(">>>> [业务开始] 正在从数据库获取所有插画作品...");
        List<IllustrationDTO> illustrationDTOS = illustrationRepository.findAll().stream()
                .map(illustrationMapper::toDto).collect(Collectors.toList());
        log.info(">>>> [业务结束] 成功转化了 {} 件插画作品。", illustrationDTOS.size());
        return illustrationDTOS;
    }

/*    private IllustrationDTO convertToDTO(Illustration entity){
        IllustrationDTO dto = new IllustrationDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setInfo(entity.getInfo());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }*/

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

    //购买文具
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyStationery(String id, Integer num) throws OutOfStockException {
        // 1. 根据 ID 找到对应的文具
        // .orElseThrow 是 Java 8 Optional 的写法，如果找不到就抛出异常
        Stationery stationery = stationeryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("该文具不存在"));

        // 2. 检查库存
        if(stationery.getStock() <= 0){
            // 触发你自定义的异常
            throw new OutOfStockException("抱歉" + stationery.getName() + "库存不足！");
        }

        // 3. 核心业务动作：减库存
        stationery.setStock(stationery.getStock() - num);

        // 4. 将改动同步回数据库
        stationeryRepository.save(stationery);

        // 💡 程序员视角：
        // 如果在这一行后面代码突然报错（比如断电了），
        // 因为有 @Transactional，上面的 setStock 动作会在数据库层面被“撤回”。
    }


    //根据类别搜索文具
    @Override
    public List<Stationery> getStationeryByCategory(String category) {
        return stationeryRepository.findByCategoryContainingIgnoreCase(category);
    }

}
