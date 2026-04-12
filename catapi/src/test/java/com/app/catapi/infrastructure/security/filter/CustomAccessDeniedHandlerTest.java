package com.app.catapi.infrastructure.security.filter;

import com.app.catapi.domain.exception.ErrorMessage;
import com.app.catapi.infrastructure.security.CustomAccessDeniedHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;

    @InjectMocks
    private CustomAccessDeniedHandler handler;

    @Test
    void shouldReturn403WithErrorMessage() throws IOException, ServletException {
        // Arrange
        AccessDeniedException ex = new AccessDeniedException("Forbidden");
        when(request.getRequestURI()).thenReturn("/api/admin/users");
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any(ErrorMessage.class)))
                .thenReturn("{\"message\":\"Not allow to consume this resource\"}");

        // Act
        handler.handle(request, response, ex);

        // Assert
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response).setContentType("application/json");
        verify(writer).write("{\"message\":\"Not allow to consume this resource\"}");
    }
}