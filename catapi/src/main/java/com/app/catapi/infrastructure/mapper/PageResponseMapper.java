package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.exception.BreedNotFoundException;
import com.app.catapi.domain.exception.ImageNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PageResponseMapper {
    private final ImageMapper imageMapper;
    public final BreedMapper breedMapper;
    public PageResponseDto<ImageDto> toImagePageResponseDto(PageResponse<Image> pageResponse) {
        return PageResponseDto.<ImageDto>builder()
                .page(pageResponse.getPage())
                .size(pageResponse.getSize())
                .content(pageResponse.getContent()
                        .stream()
                        .map(imageMapper::toImageDto)
                        .toList()

                )
                .build();
    }

    public PageResponseDto<BreedDto> toBreedPageResponseDto(PageResponse<Breed> pageResponse) {
        return PageResponseDto.<BreedDto>builder()
                .page(pageResponse.getPage())
                .size(pageResponse.getSize())
                .content(pageResponse.getContent()
                        .stream()
                        .map(breedMapper::toBreedDto)
                        .toList()
                )
                .build();
    }
    public PageResponse<Image> toImagePageResponse(ResponseEntity<List<Image>> response,
                                                             int size, int page) {
        if(response.getBody() == null || response.getBody().isEmpty()) {
            throw new ImageNotFoundException("Images not found");
        }

        List<Image> content = response.getBody();

        return PageResponse.<Image>builder()
                .content(content)
                .page(page)
                .size(size)
                .build();
    }

    public PageResponse<Breed> toBreedResponse(ResponseEntity<List<Breed>> response,
                                               int size, int page){
        if(response.getBody() == null || response.getBody().isEmpty()) {
            throw new BreedNotFoundException("Breeds not found");
        }

        List<Breed> content = response.getBody();

        return PageResponse.<Breed>builder()
                .content(content)
                .page(page)
                .size(size)
                .build();
    }

}
