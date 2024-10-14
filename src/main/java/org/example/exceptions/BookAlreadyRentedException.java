package org.example.exceptions;

public class BookAlreadyRentedException extends Exception {
    public BookAlreadyRentedException(String message) {
        super(message);
    }
}
