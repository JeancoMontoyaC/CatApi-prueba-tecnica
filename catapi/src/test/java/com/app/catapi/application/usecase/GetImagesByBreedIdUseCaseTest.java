package com.app.catapi.application.usecase;

import com.app.catapi.application.usecase.image.GetImagesByBreedIdUseCase;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.ports.ImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetImagesByBreedIdUseCaseTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private GetImagesByBreedIdUseCase getImagesByBreedIdUseCase;

    @Test
    @DisplayName("Should return PageResponse when valid breedId and pageable are provided")
    void shouldReturnPageResponse_whenValidBreedIdAndPageable() {
        // Arrange
        String breedId = "beng";
        Pageable pageable = PageRequest.of(0, 10);
        PageResponse<Image> expectedResponse = PageResponse.<Image>builder()
                .content(List.of())
                .page(0)
                .size(10)
                .build();

        when(imageRepository.getImagesByBreedId(breedId, pageable))
                .thenReturn(expectedResponse);

        // Act
        PageResponse<Image> result = getImagesByBreedIdUseCase.execute(breedId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(imageRepository, times(1)).getImagesByBreedId(breedId, pageable);
    }
}