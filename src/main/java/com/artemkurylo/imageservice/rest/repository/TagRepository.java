package com.artemkurylo.imageservice.rest.repository;

import com.artemkurylo.imageservice.rest.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
}
