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
class GetBreedByQueryUseCaseTest {

    @Mock
    private BreedRepository breedRepository;

    @InjectMocks
    private GetBreedByQueryUseCase getBreedByQueryUseCase;

    @Test
    @DisplayName("Should return PageResponse when valid query, size and page are provided")
    void shouldReturnPageResponse_whenValidQuerySizeAndPage() {
        // Arrange
        String query = "beng";
        int size = 10;
        int page = 0;
        PageResponse<Breed> expectedResponse = PageResponse.<Breed>builder()
                .content(java.util.List.of())
                .page(page)
                .size(size)
                .build();

        // Note: GetBreedByQueryUseCase calls breedRepository.getBreedByQuery(query, size, page)
        when(breedRepository.getBreedByQuery(query, size, page)).thenReturn(expectedResponse);

        // Act
        PageResponse<Breed> result = getBreedByQueryUseCase.execute(query, size, page);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(breedRepository, times(1)).getBreedByQuery(query, size, page);
    }
}

