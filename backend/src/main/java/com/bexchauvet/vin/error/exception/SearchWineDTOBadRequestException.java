package com.bexchauvet.vin.error.exception;

public class SearchWineDTOBadRequestException extends RuntimeException {

    public SearchWineDTOBadRequestException(String message) {
        super(String.format("Search wine failed because %s", message));
    }
}
