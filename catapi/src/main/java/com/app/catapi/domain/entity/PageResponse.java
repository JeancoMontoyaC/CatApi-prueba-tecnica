package com.app.catapi.domain.entity;

import com.app.catapi.domain.exception.ImageNotFoundException;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;

    public static PageResponse<Image> buildImagePageResponse(ResponseEntity<List<Image>> response,
                                                             int size, int page) {
        if(response.getBody() == null || response.getBody().isEmpty()) {
            throw new ImageNotFoundException("Images not found");
        }

        List<Image> content = response.getBody() != null
                ? response.getBody()
                : Collections.emptyList();

        return PageResponse.<Image>builder()
                .content(content)
                .page(page)
                .size(size)
                .build();
    }
}
