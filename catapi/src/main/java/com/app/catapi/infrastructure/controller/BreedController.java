package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.application.usecase.breed.GetBreedByIdUseCase;
import com.app.catapi.application.usecase.breed.GetBreedByQueryUseCase;
import com.app.catapi.application.usecase.breed.GetBreedsUseCase;
import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.infrastructure.mapper.BreedMapper;
import com.app.catapi.infrastructure.mapper.PageResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Tag(name = "Breeds", description = "Retrieve and search cat breeds from TheCatAPI")
@SecurityRequirement(name = "Bearer Authentication")
public class BreedController {

    private final GetBreedByIdUseCase  getBreedByIdUseCase;
    private final GetBreedsUseCase getBreedsUseCase;
    private final PageResponseMapper pageResponseMapper;
    private final BreedMapper breedMapper;
    private final GetBreedByQueryUseCase getBreedByQueryUseCase;

    @Operation(summary = "Get breed by ID", description = "Returns a single cat breed by its TheCatAPI ID")
    @GetMapping("/{id}")
    public ResponseEntity<BreedDto> getBreedById(
            @PathVariable @Pattern(regexp = "^[a-z]+$", message = "Invalid breed ID format") String id) {
        log.info("Getting breed by id {}", id);

        Breed breed = getBreedByIdUseCase.execute(id);
        BreedDto breedDto = breedMapper.toBreedDto(breed);

        log.info("returning information of breed with id {}", id);
        return ResponseEntity.ok(breedDto);
    }

    @Operation(summary = "Get all breeds", description = "Returns a paginated list of cat breeds. Page and size are optional, default size is 10")
    @GetMapping()
    public ResponseEntity<PageResponseDto<BreedDto>> getBreeds(
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        log.info("Getting breeds ");

        PageResponse<Breed> pageResponse = getBreedsUseCase.execute(
                pageable.getPageNumber(), pageable.getPageSize());

        PageResponseDto<BreedDto> pageResponseDto = pageResponseMapper.toBreedPageResponseDto(pageResponse);
        log.info("Breeds found");
        return ResponseEntity.ok(pageResponseDto);

    }

    @Operation(summary = "Search breeds by query", description = "Returns a paginated list of cat breeds matching the given query. Page and size are optional, default size is 10")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<BreedDto>> getBreedsByQuery(
            @RequestParam @NotBlank(message = "Query must not be blank") String query,
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        log.info("Getting breeds by query {}", query);

        PageResponse<Breed> pageResponse = getBreedByQueryUseCase
                .execute(query, pageable.getPageSize(), pageable.getPageNumber());
        PageResponseDto<BreedDto> pageResponseDto = pageResponseMapper
                .toBreedPageResponseDto(pageResponse);

        log.info("Breeds found with query {}", query);
        return ResponseEntity.ok(pageResponseDto);
    }

}
