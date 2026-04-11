package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.breed.WeightDto;
import com.app.catapi.domain.entity.Weight;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeightMapperTest {
    private final WeightMapper weightMapper = new WeightMapper();

    @Test
    void toWeightDto_shouldMapFields() {
        // Arrange
        Weight weight = new Weight();
        weight.setImperial("6 - 12");
        weight.setMetric("3 - 6");

        // Act
        WeightDto dto = weightMapper.toWeightDto(weight);

        // Assert
        assertEquals(weight.getImperial(), dto.getImperial());
        assertEquals(weight.getMetric(), dto.getMetric());
    }
}

