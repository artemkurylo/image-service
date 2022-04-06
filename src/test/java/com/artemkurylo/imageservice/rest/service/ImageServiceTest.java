package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.ImageServiceApplication;
import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.dto.TagDTO;
import com.artemkurylo.imageservice.rest.entity.Image;
import com.artemkurylo.imageservice.rest.mapper.AccountMapper;
import com.artemkurylo.imageservice.rest.mapper.ImageMapper;
import com.artemkurylo.imageservice.rest.mapper.TagMapper;
import com.artemkurylo.imageservice.rest.repository.AccountRepository;
import com.artemkurylo.imageservice.rest.repository.ImageRepository;
import com.artemkurylo.imageservice.rest.repository.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest(classes = ImageServiceApplication.class)
@Transactional
class ImageServiceTest {
    private static final TagDTO MOCK_TAG_DTO = new TagDTO(List.of("Cool"));
    private static final String MOCK_IMAGE_NAME = "Nike sneakers";
    private static final String MOCK_CONTENT_TYPE = "BMP";
    private static final long MOCK_IMAGE_SIZE = 123145L;
    private static final String MOCK_IMAGE_URL = "url.com";
    private static final String ACCOUNT_NAME = "Artem";
    private static final String EMAIL_NAME = "artem@pet.com";

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final AccountMapper accountMapper;
    private ImageService imageService;
    private final TagMapper tagMapper;

    @Autowired
    ImageServiceTest(ImageRepository imageRepository, ImageMapper imageMapper, AccountRepository accountRepository, TagRepository tagRepository, AccountMapper accountMapper, ImageService imageService, TagMapper tagMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
        this.accountMapper = accountMapper;
        this.imageService = imageService;
        this.tagMapper = tagMapper;
    }

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceImpl(imageRepository, imageMapper, accountRepository, tagRepository, tagMapper);
    }

    @Test
    void shouldCreateImage() {
        UUID accountUuid = createUser();
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        UUID imageUuid = imageService.createImage(accountUuid, imageDTO);
        Image image = imageRepository.getById(imageUuid);
        System.out.println("image = " + image);
        ImageDTO actualImageDTO = imageMapper.toDto(image);
        Assertions.assertEquals(imageDTO, actualImageDTO);
    }

    @Test
    void shouldGetImage() {
        UUID accountUuid = createUser();
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        Image image = imageMapper.toEntity(imageDTO, accountUuid,
                accountRepository,
                tagRepository, tagMapper);
        imageRepository.save(image);
        ImageDTO actualImageDTO = imageService.getImage(accountUuid, image.getUuid());
        Assertions.assertEquals(imageDTO, actualImageDTO);
    }

    @Test
    void shouldUpdateImage() {
        UUID accountUuid = createUser();
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        UUID imageUuid = imageService.createImage(accountUuid, imageDTO);
        String mockName = "Shoes";
        ImageDTO mockImageDTO = new ImageDTO(MOCK_TAG_DTO, mockName, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        imageService.updateImage(accountUuid, imageUuid, mockImageDTO);
        Assertions.assertNotEquals(imageDTO, imageService.getImage(accountUuid, imageUuid));
    }

    @Test
    void shouldDeleteImage() {
        UUID accountUuid = createUser();
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        UUID imageUuid = imageService.createImage(accountUuid, imageDTO);
        imageService.deleteImage(accountUuid, imageUuid);
        Assertions.assertThrows(NoSuchElementException.class, () -> imageService.getImage(accountUuid, imageUuid));
    }

    private UUID createUser() {
        AccountDTO accountDTO = new AccountDTO(ACCOUNT_NAME, EMAIL_NAME);
        return accountRepository.save(accountMapper.toEntity(accountDTO))
                .getUuid();
    }
}
