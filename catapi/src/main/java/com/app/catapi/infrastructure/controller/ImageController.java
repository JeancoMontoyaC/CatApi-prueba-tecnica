package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.application.usecase.GetImagesByBreedIdUseCase;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.infrastructure.mapper.ImageMapper;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.infrastructure.mapper.PageResponseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
@AllArgsConstructor
@Slf4j
public class ImageController {
    private final GetImagesByBreedIdUseCase getImagesByBreedIdUseCase;
    private final PageResponseMapper pageResponseMapper;
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<ImageDto>> getImagesByBreedId(
            @RequestParam String breedId,
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        log.info("Getting images by breed id {}", breedId);

        if (breedId == null || breedId.isEmpty()) {
            log.warn("Invalid breed id");
            return ResponseEntity.notFound().build();
        }
        PageResponse<Image> pageResponse = getImagesByBreedIdUseCase.execute(breedId, pageable);

        PageResponseDto<ImageDto> pageResponseDto = pageResponseMapper.toPageResponseDto(pageResponse);
        log.info("Images found {}", pageResponseDto.getContent());
        return ResponseEntity.ok(pageResponseDto);

    }
}
