package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.Weight;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BreedMapperTest {

    private final WeightMapper weightMapper = new WeightMapper();
    private final BreedMapper breedMapper = new BreedMapper(weightMapper);

    @Test
    void toBreedDto_shouldMapFieldsIncludingWeight() {
        // Arrange
        Weight weight = new Weight();
        weight.setImperial("6 - 12");
        weight.setMetric("3 - 6");

        Breed breed = Breed.builder()
                .id("abys")
                .name("Abyssian")
                .lifeSpan("9 -13")
                .countryCodes("EG")
                .indoor(1)
                .weight(weight)
                .build();

        // Act
        BreedDto dto = breedMapper.toBreedDto(breed);

        // Assert
        assertEquals(breed.getId(), dto.getId());
        assertEquals(breed.getName(), dto.getName());
        assertEquals(breed.getLifeSpan(), dto.getLifeSpan());
        assertEquals(breed.getCountryCodes(), dto.getCountryCodes());
        // nested weight
        assertEquals(breed.getWeight().getImperial(), dto.getWeight().getImperial());
        assertEquals(breed.getWeight().getMetric(), dto.getWeight().getMetric());
        // integer fields
        assertEquals(breed.getIndoor().intValue(), dto.getIndoor());
    }
}


