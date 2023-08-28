package com.bexchauvet.vin.error.exception;

public class WishListDTOBadRequestException extends RuntimeException {

    public WishListDTOBadRequestException(String message) {
        super(String.format("Wishlist failed because %s", message));
    }
}
