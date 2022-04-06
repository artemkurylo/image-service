package com.artemkurylo.imageservice.rest.repository;

import com.artemkurylo.imageservice.rest.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
