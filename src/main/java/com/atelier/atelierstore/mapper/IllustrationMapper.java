package com.atelier.atelierstore.mapper;

import com.atelier.atelierstore.dto.IllustrationDTO;
import com.atelier.atelierstore.model.Illustration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IllustrationMapper{
    // 只要字段名一样，MapStruct 就会自动完成映射
    IllustrationDTO toDto(Illustration entity);

    // 如果你想把 DTO 转回 Entity（用于保存数据时）
    Illustration toEntity(IllustrationDTO dto);
}
