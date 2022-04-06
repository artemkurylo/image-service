package com.artemkurylo.imageservice.rest.controller;

import com.artemkurylo.imageservice.rest.dto.ImageDTO;
import com.artemkurylo.imageservice.rest.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.version.latest}")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping("/account/image/{imageUuid}")
    public ImageDTO getImage(@RequestParam UUID accountUuid, @PathVariable UUID imageUuid) {
        return imageService.getImage(accountUuid, imageUuid);
    }

    @PostMapping("/account/{accountUuid}/image")
    public UUID createImage(@PathVariable UUID accountUuid, @RequestBody ImageDTO imageDTO) {
        return imageService.createImage(accountUuid, imageDTO);
    }

    @PutMapping("/account/image/{imageUuid}")
    public void updateImage(@RequestParam UUID accountUuid,
                            @PathVariable UUID imageUuid, @RequestBody ImageDTO imageDTO) {
        imageService.updateImage(accountUuid, imageUuid, imageDTO);
    }

    @DeleteMapping("/account/image/{imageUuid}")
    public void deleteImage(@RequestParam UUID accountUuid, @PathVariable UUID imageUuid) {
        imageService.deleteImage(accountUuid, imageUuid);
    }
}
