package com.app.catapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Breed {
    private Weight weight;
    private String id;
    private String name;
    private String description;
    private String origin;
    private String temperament;

    @JsonProperty("life_span")
    private String lifeSpan;

    @JsonProperty("alt_names")
    private String altNames;

    @JsonProperty("country_codes")
    private String countryCodes;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("wikipedia_url")
    private String wikipediaUrl;

    @JsonProperty("cfa_url")
    private String cfaUrl;

    @JsonProperty("vetstreet_url")
    private String vetstreetUrl;

    @JsonProperty("vcahospitals_url")
    private String vcahospitalsUrl;

    private Integer indoor;
    private Integer lap;
    private Integer experimental;
    private Integer hairless;
    private Integer natural;
    private Integer rare;
    private Integer rex;

    @JsonProperty("suppressed_tail")
    private Integer suppressedTail;

    @JsonProperty("short_legs")
    private Integer shortLegs;

    private Integer hypoallergenic;
    private Integer adaptability;

    @JsonProperty("affection_level")
    private Integer affectionLevel;

    @JsonProperty("child_friendly")
    private Integer childFriendly;

    @JsonProperty("cat_friendly")
    private Integer catFriendly;

    @JsonProperty("dog_friendly")
    private Integer dogFriendly;

    @JsonProperty("energy_level")
    private Integer energyLevel;

    private Integer grooming;

    @JsonProperty("health_issues")
    private Integer healthIssues;

    private Integer intelligence;

    @JsonProperty("shedding_level")
    private Integer sheddingLevel;

    @JsonProperty("social_needs")
    private Integer socialNeeds;

    @JsonProperty("stranger_friendly")
    private Integer strangerFriendly;

    private Integer vocalisation;
    private Integer bidability;

    @JsonProperty("reference_image_id")
    private String referenceImageId;
}
