package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.application.usecase.image.GetImagesByBreedIdUseCase;
import com.app.catapi.domain.entity.Image;
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
    @GetMapping()
    public ResponseEntity<PageResponseDto<ImageDto>> getImagesByBreedId(
            @RequestParam String breed_id,
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        log.info("Getting images by breed id {}", breed_id);

        if (breed_id == null || breed_id.isEmpty()) {
            log.warn("Invalid breed id");
            return ResponseEntity.notFound().build();
        }
        PageResponse<Image> pageResponse = getImagesByBreedIdUseCase.execute(breed_id, pageable);

        PageResponseDto<ImageDto> pageResponseDto = pageResponseMapper.toImagePageResponseDto(pageResponse);
        log.info("Images found {}", pageResponseDto.getContent());
        return ResponseEntity.ok(pageResponseDto);
    }
}
