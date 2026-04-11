package com.app.catapi.application.dto.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ImageDto {
    private String id;
    private String url;
    private String width;
    private String height;
}
