package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.application.usecase.GetBreedByIdUseCase;
import com.app.catapi.domain.entity.Breed;
import com.app.catapi.infrastructure.mapper.BreedMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/breeds")
@AllArgsConstructor
@Slf4j
public class BreedController {

    private final GetBreedByIdUseCase  getBreedByIdUseCase;
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
}
