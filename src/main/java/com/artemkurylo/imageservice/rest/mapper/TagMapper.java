package com.artemkurylo.imageservice.rest.mapper;

import com.artemkurylo.imageservice.rest.dto.TagDTO;
import com.artemkurylo.imageservice.rest.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toEntity(TagDTO tagDTO);
}
