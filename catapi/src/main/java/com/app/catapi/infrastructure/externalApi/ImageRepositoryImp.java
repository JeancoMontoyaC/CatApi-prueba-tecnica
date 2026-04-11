package com.app.catapi.infrastructure.externalApi;

import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.exception.ExternalServiceException;
import com.app.catapi.domain.exception.ImageNotFoundException;
import com.app.catapi.domain.ports.ImageRepository;
import com.app.catapi.domain.entity.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@AllArgsConstructor
@Service
@Slf4j
public class ImageRepositoryImp implements ImageRepository {
    private final WebClient externalApiClient;

    @Override
    public PageResponse<Image> getImagesByBreedId(String breedId, Pageable pageable) {
        log.info("Fetching images for breedId: {} - page: {}, size: {}",
                breedId, pageable.getPageNumber(), pageable.getPageSize());

        return externalApiClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .path("/images/search")
                            .queryParam("page", pageable.getPageNumber())
                            .queryParam("limit", pageable.getPageSize())
                            .queryParam("breed_ids", breedId)
                            .build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            log.error("Images with breedId {} not found", breedId);
                            return Mono.error(new ImageNotFoundException("Images not found"));
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            log.error("Service error");
                            return Mono.error(new ExternalServiceException("Error on external Api"));
                        }
                )
                .toEntityList(Image.class)
                .map(response -> PageResponse.buildImagePageResponse(
                        response, pageable.getPageSize(), pageable.getPageNumber()
                ))
                .block();
    }


}
