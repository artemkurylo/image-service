package com.artemkurylo.imageservice.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AccountInfoDTO(
        String fullName,
        String email,
        List<ImageDTO> imageList,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {
}
