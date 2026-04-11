package com.app.catapi.infrastructure.externalApi;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.exception.BreedNotFoundException;
import com.app.catapi.domain.exception.ExternalServiceException;
import com.app.catapi.domain.ports.BreedRepository;
import com.app.catapi.infrastructure.mapper.PageResponseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@AllArgsConstructor
@Service
@Slf4j
public class BreedRepositoryImp implements BreedRepository {
    private final WebClient externalApiClient;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public Breed getBreedById(String id) {
        return externalApiClient.get()
                .uri("/breeds/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            log.error("Breed with id {} not found", id);
                            return Mono.error(new BreedNotFoundException("Breed not found"));
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            log.error("Service error");
                            return Mono.error(new ExternalServiceException("Error on external Api"));
                        }
                )
                .bodyToMono(Breed.class)
                .block();
    }

    @Override
    public PageResponse<Breed> getBreeds(int page, int size) {
        log.info("Fetching breeds - page: {}, size: {}", page, size);
        return externalApiClient.get()
                .uri(uriBuilder -> {
                    URI uri =  uriBuilder
                            .path("/breeds")
                            .queryParam("page", page)
                            .queryParam("limit", size)
                            .build();
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            log.error("Service error");
                            return Mono.error(new ExternalServiceException("Error on external Api"));
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            log.error("Service error");
                            return Mono.error(new ExternalServiceException("Error on external Api"));
                        }
                )
                .toEntityList(Breed.class)
                .map(response -> pageResponseMapper.toBreedResponse(
                        response, size, page
                ))
                .block();
    }

    @Override
    public PageResponse<Breed> getBreedByQuery(String query, int page, int size) {
        log.info("Fetching breeds - page: {}, size: {} with query {}", page, size, query);
        return externalApiClient.get()
                .uri(uriBuilder -> {
                    URI uri =  uriBuilder
                            .path("/breeds/search")
                            .queryParam("page", page)
                            .queryParam("limit", size)
                            .queryParam("q", query)
                            .build();
                    return uri;
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            log.error("Service error");
                            return Mono.error(new ExternalServiceException("Error on external Api"));
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            log.error("Service error");
                            return Mono.error(new ExternalServiceException("Error on external Api"));
                        }
                )
                .toEntityList(Breed.class)
                .map(response -> pageResponseMapper.toBreedResponse(
                        response, size, page
                ))
                .block();
    }
}
