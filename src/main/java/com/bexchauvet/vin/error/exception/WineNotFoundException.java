package com.bexchauvet.vin.error.exception;

public class WineNotFoundException extends RuntimeException {

    public WineNotFoundException(String id) {
        super(String.format("Wine with ID or Name %s not found", id));
    }
}
