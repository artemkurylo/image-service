package com.artemkurylo.imageservice.rest.repository;

import com.artemkurylo.imageservice.rest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
