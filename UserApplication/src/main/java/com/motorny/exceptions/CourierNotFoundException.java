package com.motorny.exceptions;

public class CourierNotFoundException extends RuntimeException {
    public CourierNotFoundException(String message) {
        super(message);
    }
}
