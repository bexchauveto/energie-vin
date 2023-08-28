package com.bexchauvet.vin.rest;

import com.bexchauvet.vin.error.dto.ErrorDTO;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.WishlistDTO;
import com.bexchauvet.vin.service.WishlistService;
import com.bexchauvet.vin.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/wishlists")
@RestController
@Tag(name = "Wishlist", description = "The wishlist API.")
@AllArgsConstructor
@Slf4j
public class WishlistController {
    private final WishlistService wishlistService;
    private final JwtDecoder jwtDecoder;


    @Secured({Constants.ROLE_USER, Constants.ROLE_EXPERT})
    @Operation(summary = "Get all wishlist from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all the wishlist corresponding",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "wishlist not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("")
    public List<WishlistDTO> getAll(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.wishlistService.getWishlistByUserID(jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_USER, Constants.ROLE_EXPERT})
    @Operation(summary = "Get a wishlist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the wishlist corresponding",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "wishlist not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("/{id}")
    public WishlistDTO getId(@PathVariable("id") String id,
                             @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.wishlistService.getWishlistByID(id, jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_USER, Constants.ROLE_EXPERT})
    @Operation(summary = "Create a new wishlist in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "wishlist created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the wishlist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PostMapping("")
    public MessageDTO createWishlist(@RequestBody WishlistDTO wishlistDTO,
                                     @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.wishlistService.createWishlist(wishlistDTO, jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_USER, Constants.ROLE_EXPERT})
    @Operation(summary = "Update a wishlist in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "wishlist updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the wishlist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping("/{id}")
    public MessageDTO updateWishlist(@PathVariable("id") String id,
                                     @RequestBody WishlistDTO wishlistDTO,
                                     @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.wishlistService.updateWishlist(id, wishlistDTO,
                jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_USER, Constants.ROLE_EXPERT})
    @Operation(summary = "Delete a wishlist in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "wishlist delete",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the wishlist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @DeleteMapping("/{id}")
    public MessageDTO deleteWishlist(@PathVariable("id") String id,
                                     @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.wishlistService.deleteWishlist(id, jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }


}
