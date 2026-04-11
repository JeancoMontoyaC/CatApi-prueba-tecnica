package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.domain.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImageDto toImageDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .width(image.getWidth())
                .height(image.getHeight())
                .build();
    }
}
