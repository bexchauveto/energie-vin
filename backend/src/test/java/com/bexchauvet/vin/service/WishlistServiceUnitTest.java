package com.bexchauvet.vin.service;

import com.bexchauvet.lib.domain.User;
import com.bexchauvet.lib.domain.WishList;
import com.bexchauvet.lib.repository.UserRepository;
import com.bexchauvet.lib.repository.WishlistRepository;
import com.bexchauvet.vin.error.exception.WishListDTOBadRequestException;
import com.bexchauvet.vin.error.exception.WishListNotFoundException;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.WishlistDTO;
import com.bexchauvet.vin.service.Impl.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceUnitTest {

    WishlistService wishlistService;
    @Mock
    WishlistRepository wishlistRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        wishlistService = new WishlistServiceImpl(wishlistRepository, userRepository);
    }


    @Test
    @DisplayName("Test the get all wishlist from user function")
    public void testGetWishlistByUserID() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User(1L, "username", "password",
                null, null, null)));
        when(wishlistRepository.findByUserID(any(User.class)))
                .thenReturn(List.of(new WishList(null, null, "name", List.of("ROUGE"), List.of("FRANCE"),
                        List.of(2023), 0.0, 100.0, 15.0, 10.0)));
        List<WishlistDTO> expectedResult = List.of(new WishlistDTO("name", List.of("ROUGE"), List.of("FRANCE"),
                List.of(2023), 0.0, 100.0, 15.0, 10.0));
        assertEquals(expectedResult, wishlistService.getWishlistByUserID("username"));
        verify(userRepository).findByUsername(anyString());
        verify(wishlistRepository).findByUserID(any(User.class));
        verifyNoMoreInteractions(wishlistRepository, userRepository);
    }

    @Test
    @DisplayName("Test the get wishlist by id from user function")
    public void testGetWishlistByID() {
        when(userRepository.findByUsername("username2"))
                .thenReturn(Optional.of(new User(1123L, "username", "password",
                        null, null, null)));
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User(1L, "username", "password",
                        null, null, null)));
        when(wishlistRepository.findById("2"))
                .thenReturn(Optional.empty());
        when(wishlistRepository.findById("1"))
                .thenReturn(Optional.of(new WishList(null, new User(1L, "username", "password",
                        null, null, null), "name", List.of("ROUGE"), List.of("FRANCE"),
                        List.of(2023), 0.0, 100.0, 15.0, 10.0)));
        assertThrows(WishListNotFoundException.class,
                () -> wishlistService.getWishlistByID("2", "username"));
        assertThrows(WishListNotFoundException.class,
                () -> wishlistService.getWishlistByID("1", "username2"));
        WishlistDTO expectedResult = new WishlistDTO("name", List.of("ROUGE"), List.of("FRANCE"),
                List.of(2023), 0.0, 100.0, 15.0, 10.0);
        assertEquals(expectedResult, wishlistService.getWishlistByID("1", "username"));
        verify(userRepository, times(3)).findByUsername(anyString());
        verify(wishlistRepository, times(3)).findById(anyString());
        verifyNoMoreInteractions(wishlistRepository, userRepository);
    }

    @Test
    @DisplayName("Test the create wishlist function")
    public void testCreateWishlist() {
        // Bad Wine colors
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("RED"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 100.0, 0.0, 100.0), "username"));
        // Bad years
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"),
                        List.of(1800), 0.0, 100.0, 0.0, 100.0), "username"));
        // Bad years
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"),
                        List.of(21111), 0.0, 100.0, 0.0, 100.0), "username"));
        // Bad min score
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , -100.0, 100.0, 0.0, 100.0), "username"));
        // Bad max score
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 1110.0, 0.0, 100.0), "username"));
        // Max score lower than min score
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 15.0, 10.0, 0.0, 100.0), "username"));
        // Negative price
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 100.0, -120.0, 100.0), "username"));
        // Max price lower than min price
        assertThrows(WishListDTOBadRequestException.class,
                () -> wishlistService.createWishlist(new WishlistDTO("", List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 100.0, 15.0, 10.0), "username"));
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User(1L, "username", "password",
                        null, null, null)));
        when(wishlistRepository.save(any(WishList.class))).thenReturn(new WishList(1L, null, "name", List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                100.0, 0.0, 100.0));
        MessageDTO expectedResult = new MessageDTO("Wishlist with ID 1 has been created", new WishlistDTO("name", List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                100.0, 0.0, 100.0));
        MessageDTO result = wishlistService.createWishlist(new WishlistDTO("name", List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                100.0, 0.0, 100.0), "username");
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository).findByUsername(anyString());
        verify(wishlistRepository).save(any(WishList.class));
        verifyNoMoreInteractions(userRepository, wishlistRepository);
    }

    @Test
    @DisplayName("Test the update wishlist function")
    public void testUpdateWishlist() {
        when(userRepository.findByUsername("Username"))
                .thenReturn(Optional.of(new User(2L, "Username", "password",
                        null, null, null)));
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User(1L, "username", "password",
                        null, null, null)));
        when(wishlistRepository.findById("2"))
                .thenReturn(Optional.empty());
        when(wishlistRepository.findById("1"))
                .thenReturn(Optional.of(new WishList(1L, new User(1L, "username", "password",
                        null, null, null), "name", List.of("ROUGE"),
                        List.of("FRANCE"), List.of(2023), 0.0, 100.0, 0.0, 100.0)));
        assertThrows(WishListNotFoundException.class,
                () -> wishlistService.updateWishlist("2", new WishlistDTO("name",
                        List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                        100.0, 0.0, 100.0), "username"));
        assertThrows(WishListNotFoundException.class,
                () -> wishlistService.updateWishlist("1", new WishlistDTO("name",
                        List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                        100.0, 0.0, 100.0), "Username"));
        when(wishlistRepository.save(any(WishList.class))).thenReturn(new WishList(1L, null, "name", List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                100.0, 0.0, 100.0));
        MessageDTO expectedResult = new MessageDTO("Wishlist with ID 1 has been updated", new WishlistDTO("name",
                List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                100.0, 0.0, 100.0));
        MessageDTO result = wishlistService.updateWishlist("1", new WishlistDTO("name", List.of("ROUGE"), List.of(
                "FRANCE"), List.of(2023), 0.0, 100.0, 0.0, 100.0), "username");
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository, times(3)).findByUsername(anyString());
        verify(wishlistRepository, times(3)).findById(anyString());
        verify(wishlistRepository).save(any(WishList.class));
        verifyNoMoreInteractions(userRepository, wishlistRepository);
    }

    @Test
    @DisplayName("Test the delete wishlist function")
    public void testDeleteWishlist() {
        when(userRepository.findByUsername("Username"))
                .thenReturn(Optional.of(new User(2L, "Username", "password",
                        null, null, null)));
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User(1L, "username", "password",
                        null, null, null)));
        when(wishlistRepository.findById("2"))
                .thenReturn(Optional.empty());
        when(wishlistRepository.findById("1"))
                .thenReturn(Optional.of(new WishList(1L, new User(1L, "username", "password",
                        null, null, null), "name", List.of("ROUGE"),
                        List.of("FRANCE"), List.of(2023), 0.0, 100.0, 0.0, 100.0)));
        assertThrows(WishListNotFoundException.class,
                () -> wishlistService.deleteWishlist("2", "username"));
        assertThrows(WishListNotFoundException.class,
                () -> wishlistService.deleteWishlist("1", "Username"));
        doNothing().when(wishlistRepository).delete(any(WishList.class));
        MessageDTO expectedResult = new MessageDTO("Wishlist with ID 1 has been deleted", 1L);
        MessageDTO result = wishlistService.deleteWishlist("1", "username");
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository, times(3)).findByUsername(anyString());
        verify(wishlistRepository, times(3)).findById(anyString());
        verify(wishlistRepository).delete(any(WishList.class));
        verifyNoMoreInteractions(userRepository, wishlistRepository);
    }

}
