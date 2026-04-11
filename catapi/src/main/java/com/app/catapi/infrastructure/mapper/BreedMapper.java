package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.domain.entity.Breed;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BreedMapper {
    private final WeightMapper weightMapper;

    public BreedDto toBreedDto(Breed breed) {
        return BreedDto.builder()
                .id(breed.getId())
                .weight(weightMapper.toWeightDto(breed.getWeight()))
                .name(breed.getName())
                .description(breed.getDescription())
                .origin(breed.getOrigin())
                .temperament(breed.getTemperament())
                .lifeSpan(breed.getLifeSpan())
                .countryCodes(breed.getCountryCodes())
                .countryCode(breed.getCountryCode())
                .wikipediaUrl(breed.getWikipediaUrl())
                .cfaUrl(breed.getCfaUrl())
                .vetstreetUrl(breed.getVetstreetUrl())
                .vcahospitalsUrl(breed.getVcahospitalsUrl())
                .indoor(breed.getIndoor())
                .lap(breed.getLap())
                .experimental(breed.getExperimental())
                .hairless(breed.getHairless())
                .natural(breed.getNatural())
                .rare(breed.getRare())
                .rex(breed.getRex())
                .suppressedTail(breed.getSuppressedTail())
                .shortLegs(breed.getShortLegs())
                .hypoallergenic(breed.getHypoallergenic())
                .adaptability(breed.getAdaptability())
                .affectionLevel(breed.getAffectionLevel())
                .childFriendly(breed.getChildFriendly())
                .catFriendly(breed.getCatFriendly())
                .dogFriendly(breed.getDogFriendly())
                .energyLevel(breed.getEnergyLevel())
                .grooming(breed.getGrooming())
                .healthIssues(breed.getHealthIssues())
                .intelligence(breed.getIntelligence())
                .sheddingLevel(breed.getSheddingLevel())
                .socialNeeds(breed.getSocialNeeds())
                .strangerFriendly(breed.getStrangerFriendly())
                .vocalisation(breed.getVocalisation())
                .bidability(breed.getBidability())
                .referenceImageId(breed.getReferenceImageId())
                .build();
    }
}
