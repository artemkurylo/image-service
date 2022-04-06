package com.artemkurylo.imageservice.rest.controller;

import com.artemkurylo.imageservice.ImageServiceApplication;
import com.artemkurylo.imageservice.rest.dto.AccountDTO;
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
import java.util.UUID;

@SpringBootTest(classes = ImageServiceApplication.class)
@WebAppConfiguration
class AccountControllerTest {
    private static final String SLASH = "/";
    private static final String MOCK_NAME = "Artem";
    private static final String MOCK_EMAIL = "artem@pet.com";
    private static final String ACCOUNT_RESOURCE = "/account";
    private static final int SUCCESS_HTTP_CODE = 200;
    private static final int NOT_FOUND_HTTP_CODE = 404;

    @Value("${api.version.latest}")
    private String apiVersion;

    private final MockMvc mvc;

    @Autowired
    public AccountControllerTest(WebApplicationContext webApplicationContext) {
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
    void shouldReturn404Status_whenAccountNotExist() throws Exception {
        MvcResult mvcResult = getAccount(UUID.randomUUID());
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(NOT_FOUND_HTTP_CODE, status);
    }

    @Test
    void shouldRegisterAccount_whenAccountNotExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = getAccount(accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldUpdateAccount_whenAccountExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = updateAccount(accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldDeleteAccount_whenAccountExist() throws Exception {
        UUID accountUuid = registerAccount();
        MvcResult mvcResult = deleteAccount(accountUuid);
        Assertions.assertEquals(SUCCESS_HTTP_CODE, mvcResult.getResponse().getStatus());
    }

    private MvcResult deleteAccount(UUID accountUuid) throws Exception {
        String url = apiVersion.concat(ACCOUNT_RESOURCE).concat(SLASH).concat(accountUuid.toString());
        return mvc.perform(
                        MockMvcRequestBuilders
                                .delete(url))
                .andReturn();
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

    private MvcResult getAccount(UUID accountUuid) throws Exception {
        String url = apiVersion.concat(ACCOUNT_RESOURCE).concat(SLASH).concat(accountUuid.toString());
        return mvc.perform(
                        MockMvcRequestBuilders
                                .get(url)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private MvcResult updateAccount(UUID accountUuid) throws Exception {
        String mockUpdateName = "Art";
        AccountDTO mockAccountDTO = new AccountDTO(mockUpdateName, MOCK_EMAIL);
        String jsonDto = mapToJson(mockAccountDTO);
        String url = apiVersion.concat(ACCOUNT_RESOURCE).concat(SLASH).concat(accountUuid.toString());
        return mvc.perform(
                        MockMvcRequestBuilders
                                .put(url)
                                .content(jsonDto)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}
