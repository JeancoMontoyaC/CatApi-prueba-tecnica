package com.app.catapi.application.usecase;

import com.app.catapi.domain.entity.Breed;
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
class GetBreedByIdUseCaseTest {

    @Mock
    private BreedRepository breedRepository;

    @InjectMocks
    private GetBreedByIdUseCase getBreedByIdUseCase;

    @Test
    @DisplayName("Should return Breed when valid id is provided")
    void shouldReturnBreed_whenValidId() {
        // Arrange
        String id = "beng";
        Breed expectedBreed = Breed.builder()
                .id(id)
                .name("Bengal")
                .build();

        when(breedRepository.getBreedById(id)).thenReturn(expectedBreed);

        // Act
        Breed result = getBreedByIdUseCase.execute(id);

        // Assert
        assertNotNull(result);
        assertEquals(expectedBreed, result);
        verify(breedRepository, times(1)).getBreedById(id);
    }
}

