package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.AccountInfoDTO;

import java.util.UUID;

public interface AccountService {
    AccountInfoDTO getAccountInfo(UUID uuid);

    UUID createAccount(AccountDTO accountDTO);

    void updateAccountInfo(UUID uuid, AccountDTO accountDTO);

    void deleteAccount(UUID uuid);
}
