package com.app.catapi.infrastructure.security.filter;

import com.app.catapi.domain.exception.ErrorMessage;
import com.app.catapi.infrastructure.security.CustomAuthEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthEntryPointTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;

    @InjectMocks
    private CustomAuthEntryPoint entryPoint;

    @Test
    void shouldReturn401WithErrorMessage() throws IOException, ServletException {
        // Arrange
        AuthenticationException ex = new BadCredentialsException("Unauthorized");
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any(ErrorMessage.class)))
                .thenReturn("{\"message\":\"Must login before you can access this resource\"}");

        // Act
        entryPoint.commence(request, response, ex);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType("application/json");
        verify(writer).write("{\"message\":\"Must login before you can access this resource\"}");
    }
}
