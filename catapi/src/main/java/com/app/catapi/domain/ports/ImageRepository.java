package com.app.catapi.domain.ports;

import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ImageRepository {
    PageResponse<Image> getImagesByBreedId(String breedId, Pageable pageable);
}
