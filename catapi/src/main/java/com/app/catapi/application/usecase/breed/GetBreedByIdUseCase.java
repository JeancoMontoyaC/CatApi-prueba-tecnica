package com.app.catapi.application.usecase.breed;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.ports.BreedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GetBreedByIdUseCase {
    private final BreedRepository breedRepository;

    public Breed execute(String id) {
        log.info("Executing use case to get breed by id {}", id);
        return breedRepository.getBreedById(id);
    }
}
