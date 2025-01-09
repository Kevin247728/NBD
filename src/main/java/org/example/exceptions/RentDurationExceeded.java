package org.example.exceptions;

public class RentDurationExceeded extends RuntimeException {
    public RentDurationExceeded(String message) {
        super(message);
    }
}
