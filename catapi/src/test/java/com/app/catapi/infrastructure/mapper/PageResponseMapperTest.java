package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageResponseMapperTest {

    private final ImageMapper imageMapper = new ImageMapper();
    private final WeightMapper weightMapper = new WeightMapper();
    private final BreedMapper breedMapper = new BreedMapper(weightMapper);
    private final PageResponseMapper pageResponseMapper = new PageResponseMapper(imageMapper, breedMapper);

    @Test
    void toImagePageResponseDto_shouldMapContentAndPaging() {
        // Arrange
        Image image = new Image();
        image.setId("img1");
        image.setUrl("https://example.com/cat.jpg");
        image.setWidth("800");
        image.setHeight("600");

        PageResponse<Image> pageResponse = PageResponse.<Image>builder()
                .content(List.of(image))
                .page(2)
                .size(10)
                .build();

        // Act
        PageResponseDto<ImageDto> dto = pageResponseMapper.toImagePageResponseDto(pageResponse);

        // Assert
        assertEquals(pageResponse.getPage(), dto.getPage());
        assertEquals(pageResponse.getSize(), dto.getSize());
        assertEquals(1, dto.getContent().size());
        ImageDto imageDto = dto.getContent().get(0);
        assertEquals(image.getId(), imageDto.getId());
        assertEquals(image.getUrl(), imageDto.getUrl());
        assertEquals(image.getWidth(), imageDto.getWidth());
        assertEquals(image.getHeight(), imageDto.getHeight());
    }
}

