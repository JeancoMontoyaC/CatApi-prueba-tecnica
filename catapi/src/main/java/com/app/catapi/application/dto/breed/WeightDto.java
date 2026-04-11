package com.app.catapi.application.dto.breed;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class WeightDto {
    private String imperial;
    private String metric;
}
