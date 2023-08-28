package com.bexchauvet.vin.error.exception;

public class BadLoginUnauthorizedException  extends RuntimeException {

    public BadLoginUnauthorizedException() {
        super(String.format("Provided username or password are not valid"));
    }
}