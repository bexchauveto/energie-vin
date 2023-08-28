package com.bexchauvet.vin.service;

import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.WishlistDTO;

import java.util.List;

public interface WishlistService {

    List<WishlistDTO> getWishlistByUserID(String userID);

    WishlistDTO getWishlistByID(String id, String userID);

    MessageDTO createWishlist(WishlistDTO wishlistDTO, String userID);

    MessageDTO updateWishlist(String id, WishlistDTO wishlistDTO, String userID);

    MessageDTO deleteWishlist(String id, String userID);


}
