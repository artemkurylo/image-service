package com.artemkurylo.imageservice.rest.controller;

import com.artemkurylo.imageservice.ImageServiceApplication;
import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.dto.TagDTO;
import com.artemkurylo.imageservice.rest.service.AccountService;
import com.artemkurylo.imageservice.rest.service.ImageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = ImageServiceApplication.class)
@WebAppConfiguration
class SearchControllerTest {
    private static final String MOCK_IMAGE_NAME = "Nike sneakers";
    private static final String MOCK_CONTENT_TYPE = "BMP";
    private static final long MOCK_IMAGE_SIZE = 123145L;
    private static final String MOCK_IMAGE_URL = "url.com";
    private static final String ACCOUNT_NAME = "Artem";
    private static final String EMAIL_NAME = "artem@pet.com";
    private static final String TAG = "Cool";
    private static final TagDTO MOCK_TAG_DTO = new TagDTO(List.of(TAG));
    private static final int SUCCESS = 200;

    @Value("${api.version.latest}")
    private String apiVersion;
    private static final String RESOURCE_NAME = "/search";
    private static final String SLASH = "/";

    private final MockMvc mvc;
    private final ImageService imageService;
    private final AccountService accountService;

    @Autowired
    public SearchControllerTest(WebApplicationContext webApplicationContext, ImageService imageService, AccountService accountService) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.imageService = imageService;
        this.accountService = accountService;
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void shouldResponseCode200_whenFoundResults() throws Exception {
        UUID accountUuid = createAccount();
        createImage(accountUuid);
        ImageDTO searchCriteria = new ImageDTO(MOCK_TAG_DTO, null, null, null, null);
        String jsonDto = mapToJson(searchCriteria);
        String url = apiVersion
                .concat(RESOURCE_NAME)
                .concat(SLASH)
                .concat(accountUuid.toString());
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .get(url)
                                .content(jsonDto)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(SUCCESS, mvcResult.getResponse().getStatus());
    }

    private void createImage(UUID accountUuid) {
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        imageService.createImage(accountUuid, imageDTO);
    }

    private UUID createAccount() {
        AccountDTO accountDTO = new AccountDTO(ACCOUNT_NAME, EMAIL_NAME);
        return accountService.createAccount(accountDTO);
    }
}
