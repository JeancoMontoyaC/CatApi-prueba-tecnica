package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.breed.WeightDto;
import com.app.catapi.domain.entity.Weight;
import org.springframework.stereotype.Component;

@Component
public class WeightMapper {
    public WeightDto toWeightDto(Weight weight) {
        return WeightDto.builder()
                .imperial(weight.getImperial())
                .metric(weight.getMetric())
                .build();
    }
}

