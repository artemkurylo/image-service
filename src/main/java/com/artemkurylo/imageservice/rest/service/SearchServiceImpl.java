package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.entity.Image;
import com.artemkurylo.imageservice.rest.mapper.ImageMapper;
import com.artemkurylo.imageservice.rest.repository.ImageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public class SearchServiceImpl implements SearchService {
    private final ImageSearchRepository imageRepository;
    private final ImageMapper imageMapper;

    @Autowired
    public SearchServiceImpl(ImageSearchRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    @Override
    public List<ImageDTO> search(ImageDTO imageDTO, UUID accountUuid) {
        List<Image> imageList = imageRepository.findAllByAccount_Uuid(accountUuid);
        return filterImageList(imageDTO, imageList)
                .stream()
                .map(imageMapper::toDto)
                .toList();
    }

    private List<Image> filterImageList(ImageDTO imageDTO, List<Image> imageList) {
        if (imageDTO == null) {
            return imageList;
        }
        if (imageDTO.name() != null && !imageDTO.name().isBlank()) {
            imageList = imageList
                    .stream()
                    .filter(image -> image.getName().equals(imageDTO.name()))
                    .toList();
        }
        if (imageDTO.tagDTO() != null && !imageDTO.tagDTO().tagList().isEmpty()) {
            imageList = imageList
                    .stream()
                    .filter(image -> {
                        List<String> tags = image.getTag().getTagList();
                        List<String> dtoTags = imageDTO.tagDTO().tagList();
                        for (String tagName : dtoTags) {
                            if (tags.contains(tagName)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .toList();
        }
        if (imageDTO.contentType() != null && !imageDTO.contentType().isBlank()) {
            imageList = imageList
                    .stream()
                    .filter(image -> image.getContentType().equals(imageDTO.contentType()))
                    .toList();
        }
        if (imageDTO.size() != null && imageDTO.size() > 0) {
            imageList = imageList
                    .stream()
                    .filter(image -> image.getSize().equals(imageDTO.size()))
                    .toList();
        }
        if (imageDTO.resourceUrl() != null && !imageDTO.resourceUrl().isBlank()) {
            imageList = imageList
                    .stream()
                    .filter(image -> image.getResourceUrl().equals(imageDTO.resourceUrl()))
                    .toList();
        }
        return imageList;
    }
}
