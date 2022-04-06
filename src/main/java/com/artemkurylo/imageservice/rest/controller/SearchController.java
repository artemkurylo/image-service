package com.artemkurylo.imageservice.rest.controller;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.version.latest}")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search/{accountUuid}")
    public Page<ImageDTO> searchImage(@PageableDefault Pageable pageable,
                                      @RequestBody(required = false) ImageDTO imageDTO,
                                      @PathVariable UUID accountUuid) {
        List<ImageDTO> imageDTOList = searchService.search(imageDTO, accountUuid);
        return new PageImpl<>(imageDTOList, pageable, imageDTOList.size());
    }
}
