package com.artemkurylo.imageservice.rest.repository;

import com.artemkurylo.imageservice.rest.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ImageSearchRepository extends JpaRepository<Image, UUID>, JpaSpecificationExecutor<Image> {
    List<Image> findAllByAccount_Uuid(UUID accountUuid);
}
