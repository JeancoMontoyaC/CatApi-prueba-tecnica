package com.app.catapi.infrastructure.externalApi;

import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.exception.ExternalServiceException;
import com.app.catapi.domain.exception.ImageNotFoundException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageRepositoryImpTest {

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

    @InjectMocks
    private ImageRepositoryImp imageRepositoryImp;

    @Test
    void shouldReturnPageResponse_whenExternalApiSucceeds() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Image image = Image.builder()
                .url("https://example.com/cat1.jpg")
                .id("abc123").width("800").height("600").
                build();
        List<Image> images = List.of(image);
        ResponseEntity<List<Image>> responseEntity = ResponseEntity.ok(images);
        PageResponse<Image> expectedResponse = PageResponse.<Image>builder()
                .content(images)
                .page(0)
                .size(10)
                .build();

        mockWebClientChain();

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Image.class))
                .thenReturn(Mono.just(responseEntity));
        when(pageResponseMapper.toImagePageResponse(any(), eq(10), eq(0)))
                .thenReturn(expectedResponse);

        // Act
        PageResponse<Image> result = imageRepositoryImp.getImagesByBreedId("beng", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(pageResponseMapper, times(1)).toImagePageResponse(any(), eq(10), eq(0));
    }

    @Test
    void shouldThrowImageNotFoundException_whenExternalApiReturns4xx() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        mockWebClientChain();

        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            Predicate<HttpStatusCode> predicate = invocation.getArgument(0);
            Function<ClientResponse, Mono<? extends Throwable>> handler = invocation.getArgument(1);

            if (predicate.test(HttpStatus.NOT_FOUND)) {
                return responseSpec; // deja pasar para que el siguiente onStatus no interfiera
            }
            return responseSpec;
        });

        when(responseSpec.toEntityList(Image.class))
                .thenReturn(Mono.error(new ImageNotFoundException("Images not found")));

        // Act & Assert
        assertThrows(ImageNotFoundException.class,
                () -> imageRepositoryImp.getImagesByBreedId("invalid_id", pageable));
    }

    @Test
    void shouldThrowExternalServiceException_whenExternalApiReturns5xx() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        mockWebClientChain();

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Image.class))
                .thenReturn(Mono.error(new ExternalServiceException("Error on external Api")));

        // Act & Assert
        assertThrows(ExternalServiceException.class,
                () -> imageRepositoryImp.getImagesByBreedId("beng", pageable));
    }

    private void mockWebClientChain() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    @DisplayName("Should build correct URI when calling external API with given breedId and pageable parameters")
    void shouldBuildCorrectUri_whenCallingExternalApi() {
        // Arrange
        Pageable pageable = PageRequest.of(2, 5);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(any(Function.class))).thenAnswer(invocation -> {
            Function<UriBuilder, URI> uriFunction = invocation.getArgument(0);
            UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
            URI uri = uriFunction.apply(uriBuilder);

            assertTrue(uri.toString().contains("/images/search"));
            assertTrue(uri.toString().contains("page=2"));
            assertTrue(uri.toString().contains("limit=5"));
            assertTrue(uri.toString().contains("breed_ids=beng"));

            return requestHeadersSpec;
        });

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntityList(Image.class)).thenReturn(Mono.just(ResponseEntity.ok(List.of())));
        when(pageResponseMapper.toImagePageResponse(any(), eq(5), eq(2)))
                .thenReturn(PageResponse.<Image>builder().content(List.of()).page(2).size(5).build());

        // Act
        imageRepositoryImp.getImagesByBreedId("beng", pageable);
    }

}