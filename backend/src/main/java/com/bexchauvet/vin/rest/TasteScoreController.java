package com.bexchauvet.vin.rest;

import com.bexchauvet.vin.error.dto.ErrorDTO;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TasteScoreDTO;
import com.bexchauvet.vin.rest.dto.WishlistDTO;
import com.bexchauvet.vin.service.TasteScoreService;
import com.bexchauvet.vin.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/scores")
@RestController
@Tag(name = "Taste scores", description = "The Taste scores API.")
@AllArgsConstructor
public class TasteScoreController {

    private final JwtDecoder jwtDecoder;
    private TasteScoreService tasteScoreService;

    @Secured({Constants.ROLE_EXPERT})
    @Operation(summary = "Get all taste scores from expert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all the taste scores corresponding",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "taste score not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("")
    public List<TasteScoreDTO> getAll(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.tasteScoreService.getAllTasteByUsername(jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_EXPERT})
    @Operation(summary = "Get a taste score by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the taste score corresponding",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "taste score not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("/{id}")
    public TasteScoreDTO getId(@PathVariable("id") String id,
                               @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.tasteScoreService.getID(id, jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_EXPERT})
    @Operation(summary = "Create a new taste score in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Taste score created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the taste score",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PostMapping("")
    public MessageDTO createScore(@RequestBody TasteScoreDTO tasteScoreDTO,
                                  @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.tasteScoreService.createTasteScore(tasteScoreDTO,
                jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_EXPERT})
    @Operation(summary = "Update a taste score in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Taste score updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the taste score",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping("/{id}")
    public MessageDTO updateScore(@PathVariable("id") String id,
                                  @RequestBody TasteScoreDTO tasteScoreDTO,
                                  @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.tasteScoreService.updateTasteScore(id, tasteScoreDTO,
                jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }

    @Secured({Constants.ROLE_EXPERT})
    @Operation(summary = "Delete a taste score in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Taste score delete",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the taste score",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @DeleteMapping("/{id}")
    public MessageDTO deleteScore(@PathVariable("id") String id,
                                  @Parameter(hidden = true) @RequestHeader(name = "Authorization") String token) {
        return this.tasteScoreService.deleteTasteScore(id, jwtDecoder.decode(token.split(" ")[1]).getSubject());
    }


}
