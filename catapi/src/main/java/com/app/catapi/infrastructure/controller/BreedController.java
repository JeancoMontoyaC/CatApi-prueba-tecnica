package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.application.usecase.GetBreedByIdUseCase;
import com.app.catapi.application.usecase.GetBreedsUseCase;
import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.infrastructure.mapper.BreedMapper;
import com.app.catapi.infrastructure.mapper.PageResponseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/breeds")
@AllArgsConstructor
@Slf4j
public class BreedController {

    private final GetBreedByIdUseCase  getBreedByIdUseCase;
    private final GetBreedsUseCase getBreedsUseCase;
    private final PageResponseMapper pageResponseMapper;
    private final BreedMapper breedMapper;

    @GetMapping("/{id}")
    public ResponseEntity<BreedDto> getBreedById(@PathVariable String id) {
        log.info("Getting breed by id {}", id);

        if (id == null || id.isEmpty()) {
            log.warn("Invalid breed id");
            return ResponseEntity.notFound().build();
        }

        Breed breed = getBreedByIdUseCase.execute(id);

        BreedDto breedDto = breedMapper.toBreedDto(breed);

        log.info("returning information of breed {}", breedDto);
        return ResponseEntity.ok(breedDto);
    }

    @GetMapping()
    public ResponseEntity<PageResponseDto<BreedDto>> getBreeds(
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        log.info("Getting breeds ");

        PageResponse<Breed> pageResponse = getBreedsUseCase.execute(
                pageable.getPageNumber(), pageable.getPageSize());

        PageResponseDto<BreedDto> pageResponseDto = pageResponseMapper.toBreedPageResponseDto(pageResponse);
        log.info("Breeds found {}", pageResponseDto.getContent());
        return ResponseEntity.ok(pageResponseDto);

    }
}
