package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.ImageServiceApplication;
import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.dto.TagDTO;
import com.artemkurylo.imageservice.rest.mapper.ImageMapper;
import com.artemkurylo.imageservice.rest.repository.ImageSearchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = ImageServiceApplication.class)
@Transactional
class SearchServiceTest {
    private static final String MOCK_IMAGE_NAME = "Nike sneakers";
    private static final String MOCK_CONTENT_TYPE = "BMP";
    private static final long MOCK_IMAGE_SIZE = 123145L;
    private static final String MOCK_IMAGE_URL = "url.com";
    private static final String ACCOUNT_NAME = "Artem";
    private static final String EMAIL_NAME = "artem@pet.com";
    private static final String TAG = "Cool";
    private static final TagDTO MOCK_TAG_DTO = new TagDTO(List.of(TAG));

    private SearchService searchService;
    private final ImageSearchRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageService imageService;
    private final AccountService accountService;

    @Autowired
    SearchServiceTest(ImageSearchRepository imageRepository, ImageMapper imageMapper, ImageService imageService, AccountService accountService) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.imageService = imageService;
        this.accountService = accountService;
    }

    @BeforeEach
    void setUp() {
        searchService = new SearchServiceImpl(imageRepository, imageMapper);
    }

    @Test
    void shouldFindResult_whenSearchBasedOnTag() {
        UUID accountUuid = createAccount();
        UUID imageUuid = createImage(accountUuid);
        ImageDTO searchCriteria = new ImageDTO(new TagDTO(List.of(TAG)), null, null, null, null);
        List<ImageDTO> imageList = searchService.search(searchCriteria, accountUuid);
        Assertions.assertTrue(imageList.contains(
                imageService.getImage(accountUuid, imageUuid)
        ));
    }

    @Test
    void shouldFindResult_whenSearchBasedOnName() {
        UUID accountUuid = createAccount();
        UUID imageUuid = createImage(accountUuid);
        ImageDTO searchCriteria = new ImageDTO(MOCK_TAG_DTO, null, null, null, null);
        List<ImageDTO> imageList = searchService.search(searchCriteria, accountUuid);
        Assertions.assertTrue(imageList.contains(
                imageService.getImage(accountUuid, imageUuid)
        ));
    }

    @Test
    void shouldFindAllImages_whenCriteriaNull() {
        UUID accountUuid = createAccount();
        UUID imageUuid = createImage(accountUuid);
        ImageDTO searchCriteria = null;
        List<ImageDTO> imageList = searchService.search(searchCriteria, accountUuid);
        Assertions.assertTrue(imageList.contains(
                imageService.getImage(accountUuid, imageUuid)
        ));
    }

    private UUID createImage(UUID accountUuid) {
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        return imageService.createImage(accountUuid, imageDTO);
    }

    private UUID createAccount() {
        AccountDTO accountDTO = new AccountDTO(ACCOUNT_NAME, EMAIL_NAME);
        return accountService.createAccount(accountDTO);
    }
}
