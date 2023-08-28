package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.vin.domain.User;
import com.bexchauvet.vin.domain.WishList;
import com.bexchauvet.vin.error.exception.WishListDTOBadRequestException;
import com.bexchauvet.vin.error.exception.WishListNotFoundException;
import com.bexchauvet.vin.repository.UserRepository;
import com.bexchauvet.vin.repository.WishlistRepository;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.WishlistDTO;
import com.bexchauvet.vin.service.WishlistService;
import com.bexchauvet.vin.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;


    @Override
    public List<WishlistDTO> getWishlistByUserID(String userID) {
        List<WishList> wishLists = this.wishlistRepository.findByUserID(userID);
        return wishLists.stream().map(wishlist -> new WishlistDTO(wishlist.getName(), wishlist.getColors(),
                wishlist.getCountries(), wishlist.getVintages(), wishlist.getMinScore(), wishlist.getMaxScore(),
                wishlist.getMinPrice(), wishlist.getMaxPrice())).collect(Collectors.toList());
    }

    @Override
    public WishlistDTO getWishlistByID(String id, String userID) {
        User user = this.userRepository.findByUsername(userID).get();
        WishList wishlist = this.wishlistRepository.findById(id).orElseThrow(() -> new WishListNotFoundException(id));
        if (!wishlist.getUser().getUserId().equals(user.getUserId())) {
            throw new WishListNotFoundException(id);
        }
        return new WishlistDTO(wishlist.getName(), wishlist.getColors(), wishlist.getCountries(),
                wishlist.getVintages(), wishlist.getMinScore(), wishlist.getMaxScore(), wishlist.getMinPrice(),
                wishlist.getMaxPrice());
    }

    @Override
    public MessageDTO createWishlist(WishlistDTO wishlistDTO, String userID) throws WishListDTOBadRequestException {
        this.checkWishlist(wishlistDTO);
        User user = this.userRepository.findByUsername(userID).get();
        WishList wishList = this.wishlistRepository.save(new WishList(null, user, wishlistDTO.getName(),
                wishlistDTO.getColors(),
                wishlistDTO.getCountries(), wishlistDTO.getVintages(), wishlistDTO.getMinScore(),
                wishlistDTO.getMaxScore(), wishlistDTO.getMinPrice(), wishlistDTO.getMaxPrice()));
        return new MessageDTO(String.format("Wishlist with ID %s has been created", wishList.getWishlistId()),
                wishList);
    }

    @Override
    public MessageDTO updateWishlist(String id, WishlistDTO wishlistDTO, String userID) {
        this.checkWishlist(wishlistDTO);
        User user = this.userRepository.findByUsername(userID).get();
        WishList wishlist = this.wishlistRepository.findById(id).orElseThrow(() -> new WishListNotFoundException(id));
        if (!wishlist.getUser().getUserId().equals(user.getUserId())) {
            throw new WishListNotFoundException(id);
        }
        wishlist.setName(wishlistDTO.getName());
        wishlist.setColors(wishlistDTO.getColors());
        wishlist.setCountries(wishlistDTO.getCountries());
        wishlist.setVintages(wishlistDTO.getVintages());
        wishlist.setMinScore(wishlistDTO.getMinScore());
        wishlist.setMaxScore(wishlistDTO.getMaxScore());
        wishlist.setMinPrice(wishlistDTO.getMinPrice());
        wishlist.setMaxPrice(wishlistDTO.getMaxPrice());
        wishlist = this.wishlistRepository.save(wishlist);
        return new MessageDTO(String.format("Wishlist with ID %s has been updated", wishlist.getWishlistId()),
                wishlist);
    }

    @Override
    public MessageDTO deleteWishlist(String id, String userID) {
        User user = this.userRepository.findByUsername(userID).get();
        WishList wishlist = this.wishlistRepository.findById(id).orElseThrow(() -> new WishListNotFoundException(id));
        if (!wishlist.getUser().getUserId().equals(user.getUserId())) {
            throw new WishListNotFoundException(id);
        }
        this.wishlistRepository.delete(wishlist);
        return new MessageDTO(String.format("Wishlist with ID %s has been deleted", wishlist.getWishlistId()),
                wishlist.getWishlistId());
    }

    private void checkWishlist(WishlistDTO wishlistDTO) throws WishListDTOBadRequestException {
        if (!wishlistDTO.getColors().stream().allMatch(Constants.VIN_COLORS::contains)) {
            throw new WishListDTOBadRequestException("Wine colors can only be ROUGE, BLANC or ROSE");
        }
        if (!wishlistDTO.getVintages().stream().allMatch(vintage -> (vintage < LocalDate.now().getYear() && vintage > 1900))) {
            throw new WishListDTOBadRequestException("Wine vintages can only be between 1900 and " + LocalDate.now().getYear());
        }
        if (wishlistDTO.getMinScore() < 0.0) {
            throw new WishListDTOBadRequestException("The minimum score cannot be lower than 0.0");
        }
        if (wishlistDTO.getMaxScore() > 100.0) {
            throw new WishListDTOBadRequestException("The maximum score cannot be higher than 100.0");
        }
        if (wishlistDTO.getMaxScore() <= wishlistDTO.getMinScore()) {
            throw new WishListDTOBadRequestException("The maximum score cannot be lower or equals than the minimum " +
                    "score");
        }
        if (wishlistDTO.getMinPrice() < 0.0) {
            throw new WishListDTOBadRequestException("The minimum price cannot be lower than 0.0");
        }
        if (wishlistDTO.getMaxPrice() <= wishlistDTO.getMinPrice()) {
            throw new WishListDTOBadRequestException("The maximum price cannot be lower or equals than the minimum " +
                    "price");
        }
    }
}
