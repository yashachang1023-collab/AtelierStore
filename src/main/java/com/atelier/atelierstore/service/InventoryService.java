package com.atelier.atelierstore.service;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.mapper.IllustrationMapper;
import com.atelier.atelierstore.model.BaseItem;
import com.atelier.atelierstore.model.Illustration;
import com.atelier.atelierstore.model.Stationery;
import com.atelier.atelierstore.repository.IllustrationReposiry;
import com.atelier.atelierstore.repository.StationeryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private IllustrationReposiry illustrationReposiry;
    @Autowired
    private StationeryRepository stationeryRepository;
    @Autowired
    private IllustrationMapper illustrationMapper;

    //展示所有插画
    public List<IllustrationDTO> getAllIllustration(){
        log.info(">>>> [业务开始] 正在从数据库获取所有插画作品...");
        List<IllustrationDTO> illustrationDTOS = illustrationReposiry.findAll().stream()
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
    public  void addIllustration(IllustrationDTO illustrationDTO){
        Illustration illustration= illustrationMapper.toEntity(illustrationDTO);
        illustrationReposiry.save(illustration);
    }


    //删除插画
    public void deleteIllustration(String id){

        illustrationReposiry.deleteById(id);
    }

    //购买文具
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
    public List<Stationery> getStationeryByCategory(String category) {
        return stationeryRepository.findByCategoryContainingIgnoreCase(category);
    }

}
