package com.app.catapi.image;

import com.app.catapi.domain.ports.ImageRepository;
import com.app.catapi.infrastructure.externalApi.ImageRepositoryImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageRepositoryImp imageService;

    @Test
    @DisplayName("Should return PageResponseDto<ImageDto> when getImages is called with valid parameters")
    void should
}
