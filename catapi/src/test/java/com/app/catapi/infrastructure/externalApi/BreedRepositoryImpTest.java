package com.app.catapi.infrastructure.externalApi;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.exception.BreedNotFoundException;
import com.app.catapi.domain.exception.ExternalServiceException;
import com.app.catapi.infrastructure.mapper.PageResponseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BreedRepositoryImpTest {

    @Mock
    private WebClient webClient;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ClientResponse clientResponse;

    @InjectMocks
    private BreedRepositoryImp breedRepositoryImp;


    @Test
    @DisplayName("Should return")
    void shouldReturnBreed_whenExternalApiSucceeds() {
        // Arrange
        Breed expected = Breed.builder()
                .id("abys")
                .name("Abyssian")
                .lifeSpan("9 -13")
                .countryCodes("EG")
                .indoor(1)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds/{id}", "beng")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Breed.class)).thenReturn(Mono.just(expected));

        // Act
        Breed result = breedRepositoryImp.getBreedById("beng");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        verify(responseSpec, times(1)).bodyToMono(Breed.class);
    }

    @Test
    @DisplayName("Should throw BreedNotFoundException when getBreedById returns 4xx")
    void shouldThrowBreedNotFoundException_whenGetBreedByIdReturns4xx() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds/{id}", "invalid")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any()))
                .thenAnswer(invocation -> {
                    Predicate<HttpStatusCode> predicate = invocation.getArgument(0);
                    Function<ClientResponse, Mono<? extends Throwable>> handler = invocation.getArgument(1);
                    if (predicate.test(HttpStatus.NOT_FOUND)) {
                        handler.apply(clientResponse).block();
                    }
                    return responseSpec;
                })
                .thenReturn(responseSpec);

        // Act & Assert
        assertThrows(BreedNotFoundException.class,
                () -> breedRepositoryImp.getBreedById("invalid"));
    }

    @Test
    @DisplayName("Should throw ExternalServiceException when request returns 500")
    void shouldThrowExternalServiceException_whenGetBreedByIdReturns5xx() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds/{id}", "beng")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any()))
                .thenReturn(responseSpec)
                .thenAnswer(invocation -> {
                    Predicate<HttpStatusCode> predicate = invocation.getArgument(0);
                    Function<ClientResponse, Mono<? extends Throwable>> handler = invocation.getArgument(1);
                    if (predicate.test(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        handler.apply(clientResponse).block();
                    }
                    return responseSpec;
                });

        // Act & Assert
        assertThrows(ExternalServiceException.class,
                () -> breedRepositoryImp.getBreedById("beng"));
    }

    @Test
    @DisplayName("Should return response when breeds is found")
    void shouldReturnPageResponse_whenGetBreedsSucceeds() {
        // Arrange
        Breed breed = Breed.builder()
                .id("abys")
                .name("Abyssian")
                .lifeSpan("9 -13")
                .countryCodes("EG")
                .indoor(1)
                .build();
        PageResponse<Breed> expected = PageResponse.<Breed>builder()
                .content(List.of(breed))
                .page(0)
                .size(10)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Breed.class)).thenReturn(Mono.just(ResponseEntity.ok(List.of())));
        when(pageResponseMapper.toBreedResponse(any(), eq(10), eq(0))).thenReturn(expected);

        // Act
        PageResponse<Breed> result = breedRepositoryImp.getBreeds(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        verify(pageResponseMapper, times(1)).toBreedResponse(any(), eq(10), eq(0));
    }

    @Test
    @DisplayName("Should build uri correct")
    void shouldBuildCorrectUri_whenCallingGetBreeds() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenAnswer(invocation -> {
            Function<UriBuilder, URI> uriFunction = invocation.getArgument(0);
            UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
            URI uri = uriFunction.apply(uriBuilder);

            assertTrue(uri.toString().contains("/breeds"));
            assertTrue(uri.toString().contains("page=1"));
            assertTrue(uri.toString().contains("limit=5"));

            return requestHeadersSpec;
        });
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Breed.class)).thenReturn(Mono.just(ResponseEntity.ok(List.of())));
        when(pageResponseMapper.toBreedResponse(any(), eq(5), eq(1)))
                .thenReturn(PageResponse.<Breed>builder().content(List.of()).page(1).size(5).build());

        // Act
        breedRepositoryImp.getBreeds(1, 5);
    }

    @Test
    @DisplayName("Should throw external exception when request return error 400")
    void shouldThrowExternalServiceException_whenGetBreedsReturns4xx() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any()))
                .thenAnswer(invocation -> {
                    Predicate<HttpStatusCode> predicate = invocation.getArgument(0);
                    Function<ClientResponse, Mono<? extends Throwable>> handler = invocation.getArgument(1);
                    if (predicate.test(HttpStatus.BAD_REQUEST)) {
                        handler.apply(clientResponse).block();
                    }
                    return responseSpec;
                })
                .thenReturn(responseSpec);

        // Act & Assert
        assertThrows(ExternalServiceException.class,
                () -> breedRepositoryImp.getBreeds(0, 10));
    }

    @Test
    void shouldThrowExternalServiceException_whenGetBreedsReturns5xx() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any()))
                .thenReturn(responseSpec)
                .thenAnswer(invocation -> {
                    Predicate<HttpStatusCode> predicate = invocation.getArgument(0);
                    Function<ClientResponse, Mono<? extends Throwable>> handler = invocation.getArgument(1);
                    if (predicate.test(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        handler.apply(clientResponse).block();
                    }
                    return responseSpec;
                });

        // Act & Assert
        assertThrows(ExternalServiceException.class,
                () -> breedRepositoryImp.getBreeds(0, 10));
    }

    @Test
    @DisplayName("Should return response when breed is found by query")
    void shouldReturnPageResponse_whenGetBreedByQuerySucceeds() {
        // Arrange
        PageResponse<Breed> expected = PageResponse.<Breed>builder()
                .content(List.of())
                .page(0)
                .size(10)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Breed.class)).thenReturn(Mono.just(ResponseEntity.ok(List.of())));
        when(pageResponseMapper.toBreedResponse(any(), eq(10), eq(0))).thenReturn(expected);

        // Act
        PageResponse<Breed> result = breedRepositoryImp.getBreedByQuery("bengal", 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        verify(pageResponseMapper, times(1)).toBreedResponse(any(), eq(10), eq(0));
    }

    @Test
    @DisplayName("Should build the correct uri")
    void shouldBuildCorrectUri_whenCallingGetBreedByQuery() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenAnswer(invocation -> {
            Function<UriBuilder, URI> uriFunction = invocation.getArgument(0);
            UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
            URI uri = uriFunction.apply(uriBuilder);

            assertTrue(uri.toString().contains("/breeds/search"));
            assertTrue(uri.toString().contains("page=0"));
            assertTrue(uri.toString().contains("limit=10"));
            assertTrue(uri.toString().contains("q=bengal"));

            return requestHeadersSpec;
        });
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Breed.class)).thenReturn(Mono.just(ResponseEntity.ok(List.of())));
        when(pageResponseMapper.toBreedResponse(any(), eq(10), eq(0)))
                .thenReturn(PageResponse.<Breed>builder().content(List.of()).page(0).size(10).build());

        // Act
        breedRepositoryImp.getBreedByQuery("bengal", 0, 10);
    }

    @Test
    @DisplayName("should throw external service exception")
    void shouldThrowExternalServiceException_whenGetBreedByQueryReturns5xx() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any()))
                .thenReturn(responseSpec)
                .thenAnswer(invocation -> {
                    Predicate<HttpStatusCode> predicate = invocation.getArgument(0);
                    Function<ClientResponse, Mono<? extends Throwable>> handler = invocation.getArgument(1);
                    if (predicate.test(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        handler.apply(clientResponse).block();
                    }
                    return responseSpec;
                });

        // Act & Assert
        assertThrows(ExternalServiceException.class,
                () -> breedRepositoryImp.getBreedByQuery("bengal", 0, 10));
    }
}

