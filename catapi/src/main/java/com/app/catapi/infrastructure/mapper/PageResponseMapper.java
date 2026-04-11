package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PageResponseMapper {
    private final ImageMapper imageMapper;
    public PageResponseDto<ImageDto> toPageResponseDto(PageResponse<Image> pageResponse) {
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

}
