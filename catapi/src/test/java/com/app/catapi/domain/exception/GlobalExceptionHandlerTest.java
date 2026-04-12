package com.app.catapi.domain.exception;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.naming.AuthenticationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    @Test
    @DisplayName("Should return 404 when BreedNotFoundException is thrown")
    void shouldReturn404_whenBreedNotFoundExceptionIsThrown() {
        // Arrange
        BreedNotFoundException ex = new BreedNotFoundException("Breed not found");

        // Act
        ResponseEntity<String> response = handler.handleBreedNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Breed not found", response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when ImageNotFoundException is thrown")
    void shouldReturn404_whenImageNotFoundExceptionIsThrown() {
        // Arrange
        ImageNotFoundException ex = new ImageNotFoundException("Images not found");

        // Act
        ResponseEntity<String> response = handler.handleImageNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Images not found", response.getBody());
    }

    @Test
    @DisplayName("Should return 503 when ExternalServiceException is thrown")
    void shouldReturn503_whenExternalServiceExceptionIsThrown() {
        // Arrange
        ExternalServiceException ex = new ExternalServiceException("Error on external Api");

        // Act
        ResponseEntity<String> response = handler.handleExternal(ex);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Error on external Api", response.getBody());
    }

    @Test
    void handleEmailAlreadyUsed_shouldReturn409_withMessage() {
        EmailAlreadyUsedException ex = new EmailAlreadyUsedException("Email already used");

        ResponseEntity<String> response = globalExceptionHandler.handleEmailAlreadyUsed(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("Email already used");
    }

}
