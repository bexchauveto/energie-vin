package com.bexchauvet.vin.error.exception;

public class WishListNotFoundException extends RuntimeException {

    public WishListNotFoundException(String id) {
        super(String.format("Wishlist with ID %s not found", id));
    }
}
