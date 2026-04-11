package com.app.catapi.domain.ports;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;

public interface BreedRepository {
    Breed getBreedById(String id);
    PageResponse<Breed> getBreeds(int page, int size);
}
