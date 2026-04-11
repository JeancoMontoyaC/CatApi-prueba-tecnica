package com.app.catapi.application.usecase.breed;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.ports.BreedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GetBreedsUseCase {
    private final BreedRepository breedRepository;

    public PageResponse<Breed> execute(int page, int size){
        log.info("Received request to get breeds");
        return breedRepository.getBreeds(page, size);
    }

}
