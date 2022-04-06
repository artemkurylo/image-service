package com.artemkurylo.imageservice.rest.controller;

import com.artemkurylo.imageservice.ImageServiceApplication;
import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.dto.TagDTO;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = ImageServiceApplication.class)
@WebAppConfiguration
class ImageControllerTest {
    private static final String MOCK_NAME = "Artem";
    private static final String MOCK_EMAIL = "artem@pet.com";
    private static final TagDTO MOCK_TAG_DTO = new TagDTO(List.of("Cool"));
    private static final String MOCK_IMAGE_NAME = "Nike sneakers";
    private static final String MOCK_CONTENT_TYPE = "BMP";
    private static final long MOCK_IMAGE_SIZE = 123145L;
    private static final String MOCK_IMAGE_URL = "url.com";
    private static final String SLASH = "/";
    private static final String ACCOUNT_RESOURCE = "/account";
    private static final String IMAGE_RESOURCE = "/image";
    private static final String ACCOUNT_UUID_PARAM = "accountUuid";
    private static final int SUCCESS_HTTP_CODE = 200;
    private static final int NOT_FOUND_HTTP_CODE = 404;

    @Value("${api.version.latest}")
    private String apiVersion;

    private final MockMvc mvc;

    @Autowired
    public ImageControllerTest(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    void shouldReturn404Status_whenImageNotExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = getImage(UUID.randomUUID(), accountUuid);
        Assertions.assertEquals(NOT_FOUND_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldRegisterImage_whenAccountExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = registerImage(accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldReturnRegisteredImage_whenAccountExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = registerImage(accountUuid);
        UUID imageUuid = mapFromJson(mvcResult.getResponse().getContentAsString(), UUID.class);
        mvcResult = getImage(imageUuid, accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldUpdateImage_whenAccountExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = registerImage(accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
        UUID imageUuid = mapFromJson(mvcResult.getResponse().getContentAsString(), UUID.class);
        mvcResult = updateImage(imageUuid, accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldDeleteImage_whenAccountExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = registerImage(accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
        UUID imageUuid = mapFromJson(mvcResult.getResponse().getContentAsString(), UUID.class);
        mvcResult = deleteAccount(imageUuid, accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    private UUID registerAccount() throws Exception {
        AccountDTO mockAccountDTO = new AccountDTO(MOCK_NAME, MOCK_EMAIL);
        String jsonDto = mapToJson(mockAccountDTO);
        String url = apiVersion.concat(ACCOUNT_RESOURCE);
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .content(jsonDto)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int success = 200;
        Assertions.assertEquals(success, mvcResult.getResponse().getStatus());
        return mapFromJson(mvcResult.getResponse().getContentAsString(), UUID.class);
    }

    private MvcResult getImage(UUID imageUuid, UUID accountUuid) throws Exception {
        String url = apiVersion.concat(ACCOUNT_RESOURCE)
                .concat(IMAGE_RESOURCE).concat(SLASH).concat(imageUuid.toString());
        return mvc.perform(
                        MockMvcRequestBuilders
                                .get(url)
                                .param(ACCOUNT_UUID_PARAM, accountUuid.toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private MvcResult registerImage(UUID accountUuid) throws Exception {
        ImageDTO imageDTO = new ImageDTO(MOCK_TAG_DTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        String url = apiVersion.concat(ACCOUNT_RESOURCE).concat(SLASH)
                .concat(accountUuid.toString())
                .concat(IMAGE_RESOURCE);
        String jsonDto = mapToJson(imageDTO);
        return mvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .content(jsonDto)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private MvcResult updateImage(UUID imageUuid, UUID accountUuid) throws Exception {
        TagDTO tagDTO = new TagDTO(List.of("Shoes"));
        ImageDTO imageDTO = new ImageDTO(tagDTO, MOCK_IMAGE_NAME, MOCK_CONTENT_TYPE, MOCK_IMAGE_SIZE, MOCK_IMAGE_URL);
        String url = apiVersion.concat(ACCOUNT_RESOURCE)
                .concat(IMAGE_RESOURCE)
                .concat(SLASH)
                .concat(imageUuid.toString());
        String jsonDto = mapToJson(imageDTO);
        return mvc.perform(
                        MockMvcRequestBuilders
                                .put(url)
                                .param(ACCOUNT_UUID_PARAM, accountUuid.toString())
                                .content(jsonDto)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private MvcResult deleteAccount(UUID imageUuid, UUID accountUuid) throws Exception {
        String url = apiVersion.concat(ACCOUNT_RESOURCE)
                .concat(IMAGE_RESOURCE)
                .concat(SLASH)
                .concat(imageUuid.toString());
        return mvc.perform(
                        MockMvcRequestBuilders
                                .delete(url)
                                .param(ACCOUNT_UUID_PARAM, accountUuid.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}
