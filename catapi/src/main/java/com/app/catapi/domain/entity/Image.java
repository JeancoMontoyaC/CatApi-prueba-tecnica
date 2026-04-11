package com.app.catapi.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    private String id;
    private String url;
    private String width;
    private String height;
}
