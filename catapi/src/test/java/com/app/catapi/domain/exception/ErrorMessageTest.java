package com.app.catapi.domain.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ErrorMessageTest {

    @Test
    void constructor_withoutErrors_shouldMapFieldsAndInitEmptyErrors() {
        ErrorMessage errorMessage = new ErrorMessage("message", "ExceptionType", "/api/v1/cats");

        assertThat(errorMessage.getMessage()).isEqualTo("message");
        assertThat(errorMessage.getException()).isEqualTo("ExceptionType");
        assertThat(errorMessage.getPath()).isEqualTo("/api/v1/cats");
    }

    @Test
    void constructor_withErrors_shouldMapAllFields() {
        Map<String, String> errors = Map.of("email", "Invalid email", "password", "Too short");
        ErrorMessage errorMessage = new ErrorMessage("message", "ExceptionType", "/api/v1/cats", errors);

        assertThat(errorMessage.getMessage()).isEqualTo("message");
        assertThat(errorMessage.getException()).isEqualTo("ExceptionType");
        assertThat(errorMessage.getPath()).isEqualTo("/api/v1/cats");
    }

    @Test
    void setters_shouldUpdateFields() {
        ErrorMessage errorMessage = new ErrorMessage("message", "ExceptionType", "/api/v1/cats");

        errorMessage.setMessage("new message");
        errorMessage.setException("NewException");
        errorMessage.setPath("/api/v1/new");
        errorMessage.setErrors(Map.of("field", "error"));

        assertThat(errorMessage.getMessage()).isEqualTo("new message");
        assertThat(errorMessage.getException()).isEqualTo("NewException");
        assertThat(errorMessage.getPath()).isEqualTo("/api/v1/new");
    }
}
