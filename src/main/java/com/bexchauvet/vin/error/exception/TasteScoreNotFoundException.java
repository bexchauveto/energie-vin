package com.bexchauvet.vin.error.exception;

public class TasteScoreNotFoundException extends RuntimeException {

    public TasteScoreNotFoundException(String id) {
        super(String.format("Taste score with ID %s not found", id));
    }
}