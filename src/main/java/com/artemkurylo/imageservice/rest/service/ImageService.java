package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;

import java.util.UUID;

public interface ImageService {
    ImageDTO getImage(UUID accountUuid, UUID imageUuid);

    UUID createImage(UUID accountUuid, ImageDTO imageDTO);

    void updateImage(UUID accountUuid, UUID imageUuid, ImageDTO imageDTO);

    void deleteImage(UUID accountUuid, UUID imageUuid);

}
