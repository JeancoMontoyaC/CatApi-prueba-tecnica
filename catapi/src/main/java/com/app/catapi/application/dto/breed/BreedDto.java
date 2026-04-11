package com.app.catapi.application.dto.breed;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BreedDto {
    private WeightDto weight;
    private String id;
    private String name;
    private String description;
    private String origin;
    private String temperament;
    private String lifeSpan;
    private String countryCodes;
    private String countryCode;
    private String wikipediaUrl;
    private String cfaUrl;
    private String vetstreetUrl;
    private String vcahospitalsUrl;

    private Integer indoor;
    private Integer lap;
    private Integer experimental;
    private Integer hairless;
    private Integer natural;
    private Integer rare;
    private Integer rex;
    private Integer suppressedTail;
    private Integer shortLegs;
    private Integer hypoallergenic;

    private Integer adaptability;
    private Integer affectionLevel;
    private Integer childFriendly;
    private Integer catFriendly;
    private Integer dogFriendly;
    private Integer energyLevel;
    private Integer grooming;
    private Integer healthIssues;
    private Integer intelligence;
    private Integer sheddingLevel;
    private Integer socialNeeds;
    private Integer strangerFriendly;
    private Integer vocalisation;
    private Integer bidability;

    private String referenceImageId;
}
