package com.app.catapi.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Weight {
    private String imperial;
    private String metric;
}
