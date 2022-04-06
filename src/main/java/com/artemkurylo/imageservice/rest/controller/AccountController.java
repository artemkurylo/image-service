package com.artemkurylo.imageservice.rest.controller;

import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.AccountInfoDTO;
import com.artemkurylo.imageservice.rest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.version.latest}")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/{uuid}")
    public AccountInfoDTO getAccount(@PathVariable UUID uuid) {
        return accountService.getAccountInfo(uuid);
    }

    @PostMapping("/account")
    public UUID createAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO);
    }

    @PutMapping("/account/{uuid}")
    public void updateAccount(@PathVariable UUID uuid, @RequestBody AccountDTO accountDTO) {
        accountService.updateAccountInfo(uuid, accountDTO);
    }

    @DeleteMapping("/account/{uuid}")
    public void deleteAccount(@PathVariable UUID uuid) {
        accountService.deleteAccount(uuid);
    }
}
