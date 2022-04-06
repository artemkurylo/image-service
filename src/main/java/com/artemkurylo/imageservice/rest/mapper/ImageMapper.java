package com.artemkurylo.imageservice.rest.mapper;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.dto.TagDTO;
import com.artemkurylo.imageservice.rest.entity.Account;
import com.artemkurylo.imageservice.rest.entity.Image;
import com.artemkurylo.imageservice.rest.entity.Tag;
import com.artemkurylo.imageservice.rest.repository.AccountRepository;
import com.artemkurylo.imageservice.rest.repository.TagRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(target = "tagDTO", source = "image.tag")
    ImageDTO toDto(Image image);

    @Mapping(target = "account", source = "accountUuid", qualifiedByName = "mapAccount")
    @Mapping(target = "tag", source = "imageDTO.tagDTO", qualifiedByName = "mapTag")
    @Mapping(target = "uuid", source = "imageUuid")
    Image toEntity(ImageDTO imageDTO, UUID imageUuid,
                   UUID accountUuid, @Context AccountRepository accountRepository,
                   @Context TagRepository tagRepository, @Context TagMapper tagMapper);

    @Mapping(target = "account", source = "accountUuid", qualifiedByName = "mapAccount")
    @Mapping(target = "tag", source = "imageDTO.tagDTO", qualifiedByName = "mapTag")
    Image toEntity(ImageDTO imageDTO,
                   UUID accountUuid, @Context AccountRepository accountRepository,
                   @Context TagRepository tagRepository, @Context TagMapper tagMapper);

    Image toEntity(ImageDTO imageDTO);

    @Named("mapAccount")
    default Account mapAccount(UUID accountUuid, @Context AccountRepository accountRepository) {
        return accountRepository.getById(accountUuid);
    }


    @Named("mapTag")
    default Tag mapTag(TagDTO tagDTO, @Context TagRepository tagRepository, @Context TagMapper tagMapper) {
        if (tagDTO == null) {
            return null;
        }
        Tag tag = tagMapper.toEntity(tagDTO);
        tagRepository.save(tag);
        return tagRepository.getById(tag.getUuid());
    }
}
