package com.artemkurylo.imageservice.rest.dto;

public record ImageDTO(
        TagDTO tagDTO,
        String name,
        String contentType,
        Long size,
        String resourceUrl
) {
}
