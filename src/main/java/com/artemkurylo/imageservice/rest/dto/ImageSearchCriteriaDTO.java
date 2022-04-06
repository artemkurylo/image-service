package com.artemkurylo.imageservice.rest.dto;

public record ImageSearchCriteriaDTO(String tag,
                                     String name,
                                     String contentType,
                                     Long size,
                                     String resourceUrl) {
}
