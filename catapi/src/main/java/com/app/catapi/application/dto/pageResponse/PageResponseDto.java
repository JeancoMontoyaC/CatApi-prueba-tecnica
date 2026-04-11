package com.app.catapi.application.dto.pageResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PageResponseDto <T> {
    private List<T> content;
    private int page;
    private int size;
}
