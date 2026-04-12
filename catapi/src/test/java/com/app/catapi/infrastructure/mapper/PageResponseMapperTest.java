package com.app.catapi.infrastructure.mapper;

import com.app.catapi.application.dto.breed.BreedDto;
import com.app.catapi.application.dto.image.ImageDto;
import com.app.catapi.application.dto.pageResponse.PageResponseDto;
import com.app.catapi.domain.entity.Breed;
import com.app.catapi.domain.entity.Image;
import com.app.catapi.domain.entity.PageResponse;
import com.app.catapi.domain.entity.Weight;
import com.app.catapi.domain.exception.BreedNotFoundException;
import com.app.catapi.domain.exception.ImageNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PageResponseMapperTest {

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private BreedMapper breedMapper;

    private PageResponseMapper pageResponseMapper;

    @BeforeEach
    void setUp() {
        pageResponseMapper = new PageResponseMapper(imageMapper, breedMapper);
    }


        @Test
        void toImagePageResponse_shouldReturnPageResponse_whenBodyIsValid() {
            Image image = Image.builder().id("img1").url("http://img.com").build();
            ResponseEntity<List<Image>> response = ResponseEntity.ok(List.of(image));

            PageResponse<Image> result = pageResponseMapper.toImagePageResponse(response, 10, 0);

            assertThat(result.getPage()).isEqualTo(0);
            assertThat(result.getSize()).isEqualTo(10);
        }

            @Test
            void toImagePageResponseDto_shouldMapContentAndPagination() {
                Image image = Image.builder().id("img1").url("http://img.com").build();
                ImageDto imageDto = ImageDto.builder().id("img1").url("http://img.com").build();

                PageResponse<Image> pageResponse = PageResponse.<Image>builder()
                        .content(List.of(image))
                        .page(2)
                        .size(5)
                        .build();

                when(imageMapper.toImageDto(image)).thenReturn(imageDto);

                PageResponseDto<ImageDto> result = pageResponseMapper.toImagePageResponseDto(pageResponse);

                assertThat(result.getPage()).isEqualTo(2);
                assertThat(result.getSize()).isEqualTo(5);
                assertThat(result.getContent().get(0).getId()).isEqualTo(imageDto.getId());
            }

        @Test
        void toImagePageResponse_shouldThrow_whenBodyIsNull() {
            ResponseEntity<List<Image>> response = ResponseEntity.ok(null);

            assertThatThrownBy(() -> pageResponseMapper.toImagePageResponse(response, 10, 0))
                    .isInstanceOf(ImageNotFoundException.class)
                    .hasMessage("Images not found");
        }

        @Test
        void toImagePageResponse_shouldThrow_whenBodyIsEmpty() {
            ResponseEntity<List<Image>> response = ResponseEntity.ok(List.of());

            assertThatThrownBy(() -> pageResponseMapper.toImagePageResponse(response, 10, 0))
                    .isInstanceOf(ImageNotFoundException.class)
                    .hasMessage("Images not found");
        }


        @Test
        void toBreedResponse_shouldReturnPageResponse_whenBodyIsValid() {
            Breed breed = Breed.builder().id("1").name("Siamese").build();
            ResponseEntity<List<Breed>> response = ResponseEntity.ok(List.of(breed));

            PageResponse<Breed> result = pageResponseMapper.toBreedResponse(response, 10, 0);

            assertThat(result.getPage()).isEqualTo(0);
            assertThat(result.getSize()).isEqualTo(10);
        }

            @Test
            void toBreedPageResponseDto_shouldMapContentAndPagination() {
                Breed breed = Breed.builder().id("1").name("Siamese").build();
                BreedDto breedDto = BreedDto.builder().id("1").name("Siamese").build();

                PageResponse<Breed> pageResponse = PageResponse.<Breed>builder()
                        .content(List.of(breed))
                        .page(1)
                        .size(20)
                        .build();

                when(breedMapper.toBreedDto(breed)).thenReturn(breedDto);

                PageResponseDto<BreedDto> result = pageResponseMapper.toBreedPageResponseDto(pageResponse);

                assertThat(result.getPage()).isEqualTo(1);
                assertThat(result.getSize()).isEqualTo(20);
                assertThat(result.getContent().get(0).getId()).isEqualTo(breedDto.getId());
            }

        @Test
        void toBreedResponse_shouldThrow_whenBodyIsNull() {
            ResponseEntity<List<Breed>> response = ResponseEntity.ok(null);

            assertThatThrownBy(() -> pageResponseMapper.toBreedResponse(response, 10, 0))
                    .isInstanceOf(BreedNotFoundException.class)
                    .hasMessage("Breeds not found");
        }

        @Test
        void toBreedResponse_shouldThrow_whenBodyIsEmpty() {
            ResponseEntity<List<Breed>> response = ResponseEntity.ok(List.of());

            assertThatThrownBy(() -> pageResponseMapper.toBreedResponse(response, 10, 0))
                    .isInstanceOf(BreedNotFoundException.class)
                    .hasMessage("Breeds not found");
        }
}

