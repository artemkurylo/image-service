package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.AccountInfoDTO;
import com.artemkurylo.imageservice.rest.entity.Account;
import com.artemkurylo.imageservice.rest.mapper.AccountMapper;
import com.artemkurylo.imageservice.rest.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private static final String USER_NOT_FOUND = "User not found";
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public UUID createAccount(AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);
        accountRepository.save(account);
        return account.getUuid();
    }

    @Override
    public AccountInfoDTO getAccountInfo(UUID uuid) {
        if (accountRepository.existsById(uuid)) {
            return accountMapper.toDto(accountRepository.getById(uuid));
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }

    @Override
    public void updateAccountInfo(UUID uuid, AccountDTO accountDTO) {
        if (accountRepository.existsById(uuid)) {
            Account account = accountMapper.toEntity(accountDTO, uuid);
            accountRepository.save(account);
            return;
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }

    @Override
    public void deleteAccount(UUID uuid) {
        if (accountRepository.existsById(uuid)) {
            accountRepository.deleteById(uuid);
            return;
        }
        throw new NoSuchElementException(USER_NOT_FOUND);
    }
}
