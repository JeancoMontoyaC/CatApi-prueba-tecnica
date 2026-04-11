package com.app.catapi.application.usecase.image;

import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.ports.ImageRepository;
import com.app.catapi.domain.entity.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GetImagesByBreedIdUseCase {
    private final ImageRepository imageRepository;

    public PageResponse<Image> execute(String breedId, Pageable pageable) {
        log.info("Received request to get images by breed id {}", breedId);
        return imageRepository.getImagesByBreedId(breedId, pageable);
    }
}
