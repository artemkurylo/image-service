package com.artemkurylo.imageservice.rest.service;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;

import java.util.List;
import java.util.UUID;

public interface SearchService {
    List<ImageDTO> search(ImageDTO imageDTO, UUID accountUuid);
}
