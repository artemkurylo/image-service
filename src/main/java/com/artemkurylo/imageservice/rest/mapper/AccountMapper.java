package com.artemkurylo.imageservice.rest.mapper;

import com.artemkurylo.imageservice.rest.dto.AccountDTO;
import com.artemkurylo.imageservice.rest.dto.AccountInfoDTO;
import com.artemkurylo.imageservice.rest.entity.Account;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDTO accountDTO);

    AccountInfoDTO toDto(Account account);

    Account toEntity(AccountDTO accountDTO, UUID uuid);
}
