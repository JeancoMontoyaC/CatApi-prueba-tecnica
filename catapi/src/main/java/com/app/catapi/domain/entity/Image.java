package com.app.catapi.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Image {
    private String id;
    private String url;
    private String width;
    private String height;
}
