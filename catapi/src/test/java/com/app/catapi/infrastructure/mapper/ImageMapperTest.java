package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.domain.entity.Image;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageMapperTest {

    private final ImageMapper imageMapper = new ImageMapper();

    @Test
    void toImageDto_shouldMapAllFields() {
        // Arrange
        Image image = new Image();
        image.setId("img123");
        image.setUrl("https://example.com/cat.jpg");
        image.setWidth("800");
        image.setHeight("600");

        // Act
        ImageDto dto = imageMapper.toImageDto(image);

        // Assert
        assertEquals(image.getId(), dto.getId());
        assertEquals(image.getUrl(), dto.getUrl());
        assertEquals(image.getWidth(), dto.getWidth());
        assertEquals(image.getHeight(), dto.getHeight());
    }
}

