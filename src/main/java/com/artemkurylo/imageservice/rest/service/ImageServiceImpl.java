package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.entity.Image;
import com.artemkurylo.imageservice.rest.exception.ForbiddenResourceException;
import com.artemkurylo.imageservice.rest.mapper.ImageMapper;
import com.artemkurylo.imageservice.rest.mapper.TagMapper;
import com.artemkurylo.imageservice.rest.repository.AccountRepository;
import com.artemkurylo.imageservice.rest.repository.ImageRepository;
import com.artemkurylo.imageservice.rest.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private static final String USER_NOT_FOUND = "User not found";
    private static final String IMAGE_NOT_FOUND = "Image not found";
    private static final String FORBIDDEN = "Forbidden access";

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper, AccountRepository accountRepository, TagRepository tagRepository, TagMapper tagMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public ImageDTO getImage(UUID accountUuid, UUID imageUuid) {
        if (accountRepository.existsById(accountUuid)) {
            if (imageRepository.existsById(imageUuid)) {
                return imageMapper.toDto(imageRepository.getById(imageUuid));
            }
            throw new NoSuchElementException(IMAGE_NOT_FOUND);
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }

    @Override
    public UUID createImage(UUID accountUuid, ImageDTO imageDTO) {
        if (accountRepository.existsById(accountUuid)) {
            Image image = imageMapper.toEntity(imageDTO, accountUuid,
                    accountRepository,
                    tagRepository, tagMapper);
            imageRepository.save(image);
            return image.getUuid();
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }

    @Override
    public void updateImage(UUID accountUuid, UUID imageUuid, ImageDTO imageDTO) {
        if (accountRepository.existsById(accountUuid)) {
            if (imageRepository.getById(imageUuid).getAccount().getUuid().equals(accountUuid)) {
                if (imageRepository.existsById(imageUuid)) {
                    Image image = imageMapper.toEntity(imageDTO, imageUuid,
                            accountUuid, accountRepository,
                            tagRepository, tagMapper);
                    imageRepository.save(image);
                    return;
                }
                throw new NoSuchElementException(IMAGE_NOT_FOUND);
            }
            throw new ForbiddenResourceException(FORBIDDEN);
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }

    @Override
    public void deleteImage(UUID accountUuid, UUID imageUuid) {
        if (accountRepository.existsById(accountUuid)) {
            if (imageRepository.getById(imageUuid).getAccount().getUuid().equals(accountUuid)) {
                if (imageRepository.existsById(imageUuid)) {
                    imageRepository.deleteById(imageUuid);
                    return;
                }
                throw new NoSuchElementException(IMAGE_NOT_FOUND);
            }
            throw new ForbiddenResourceException(FORBIDDEN);
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }
}
