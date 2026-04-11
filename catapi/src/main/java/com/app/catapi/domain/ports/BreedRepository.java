package com.app.catapi.domain.ports;

import com.app.catapi.domain.entity.Breed;

public interface BreedRepository {
    Breed getBreedById(String id);
}
