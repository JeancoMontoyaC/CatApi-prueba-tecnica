package com.app.catapi.application.usecase;

import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.ports.BreedRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBreedsUseCaseTest {

    @Mock
    private BreedRepository breedRepository;

    @InjectMocks
    private GetBreedsUseCase getBreedsUseCase;

    @Test
    @DisplayName("Should return PageResponse when valid page and size are provided")
    void shouldReturnPageResponse_whenValidPageAndSize() {
        // Arrange
        int page = 0;
        int size = 10;
        PageResponse<Breed> expectedResponse = PageResponse.<Breed>builder()
                .content(java.util.List.of())
                .page(page)
                .size(size)
                .build();

        when(breedRepository.getBreeds(page, size)).thenReturn(expectedResponse);

        // Act
        PageResponse<Breed> result = getBreedsUseCase.execute(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(breedRepository, times(1)).getBreeds(page, size);
    }
}

