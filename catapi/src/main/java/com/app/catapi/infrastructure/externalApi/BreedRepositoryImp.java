package com.app.catapi.infrastructure.externalApi;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.exception.BreedNotFoundException;
import com.app.catapi.domain.exception.ExternalServiceException;
import com.app.catapi.domain.ports.BreedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
@Slf4j
public class BreedRepositoryImp implements BreedRepository {
    private final WebClient externalApiClient;

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
}
