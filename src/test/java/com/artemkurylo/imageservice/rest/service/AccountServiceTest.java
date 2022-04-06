package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.ImageServiceApplication;
import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.AccountInfoDTO;
import com.artemkurylo.imageservice.rest.entity.Account;
import com.artemkurylo.imageservice.rest.mapper.AccountMapper;
import com.artemkurylo.imageservice.rest.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest(classes = ImageServiceApplication.class)
@Transactional
class AccountServiceTest {
    private static final String ACCOUNT_NAME = "Artem";
    private static final String EMAIL_NAME = "artem@pet.com";

    private AccountService accountService;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    AccountServiceTest(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository, accountMapper);
    }

    @Test
    void shouldCreateUser() {
        AccountDTO accountDTO = new AccountDTO(ACCOUNT_NAME, EMAIL_NAME);
        UUID accountUuid = accountService.createAccount(accountDTO);
        Account createdAccount = accountRepository.getById(accountUuid);
        Assertions.assertNotNull(createdAccount);
    }

    @Test
    void shouldGetCreatedUser() {
        UUID accountUuid = createUser();
        AccountInfoDTO accountInfoDTO = accountService.getAccountInfo(accountUuid);
        Assertions.assertEquals(ACCOUNT_NAME, accountInfoDTO.fullName());
        Assertions.assertEquals(EMAIL_NAME, accountInfoDTO.email());
    }

    @Test
    void shouldUpdateUser() {
        UUID accountUuid = createUser();
        String mockName = "Art";
        AccountDTO accountDTO = new AccountDTO(mockName, EMAIL_NAME);
        accountService.updateAccountInfo(accountUuid, accountDTO);
        AccountInfoDTO accountInfoDTO = accountService.getAccountInfo(accountUuid);
        Assertions.assertEquals(mockName, accountInfoDTO.fullName());
    }

    @Test
    void shouldDeleteUser() {
        UUID accountUuid = createUser();
        accountService.deleteAccount(accountUuid);
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.getAccountInfo(accountUuid));
    }

    private UUID createUser() {
        AccountDTO accountDTO = new AccountDTO(ACCOUNT_NAME, EMAIL_NAME);
        return accountService.createAccount(accountDTO);
    }
}
