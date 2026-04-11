package com.app.catapi.application.usecase;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.ports.BreedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GetBreedByQueryUseCase {
    private final BreedRepository breedRepository;

    public PageResponse<Breed> execute(String query, int size, int page) {
        log.info("Executing use case to get breed by query {}", query);
        return breedRepository.getBreedByQuery(query, size, page);
    }

}
