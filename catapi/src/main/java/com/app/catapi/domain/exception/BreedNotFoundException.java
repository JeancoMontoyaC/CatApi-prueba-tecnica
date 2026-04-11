package com.app.catapi.domain.exception;

public class BreedNotFoundException extends RuntimeException {
    public BreedNotFoundException(String msg) { super(msg); }
}
