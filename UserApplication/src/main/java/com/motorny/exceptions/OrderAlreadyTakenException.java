package com.motorny.exceptions;

public class OrderAlreadyTakenException extends RuntimeException {
    public OrderAlreadyTakenException(String message) {
        super(message);
    }
}
