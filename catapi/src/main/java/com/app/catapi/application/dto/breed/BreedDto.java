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

    private int indoor;
    private int lap;
    private int experimental;
    private int hairless;
    private int natural;
    private int rare;
    private int rex;
    private int suppressedTail;
    private int shortLegs;
    private int hypoallergenic;

    private int adaptability;
    private int affectionLevel;
    private int childFriendly;
    private int catFriendly;
    private int dogFriendly;
    private int energyLevel;
    private int grooming;
    private int healthIssues;
    private int intelligence;
    private int sheddingLevel;
    private int socialNeeds;
    private int strangerFriendly;
    private int vocalisation;
    private int bidability;

    private String referenceImageId;
}
