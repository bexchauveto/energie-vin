package com.bexchauvet.vin.rest;


import com.bexchauvet.vin.error.dto.ErrorDTO;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TokenDTO;
import com.bexchauvet.vin.rest.dto.UserDTO;
import com.bexchauvet.vin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@Tag(name = "Authentication", description = "The Authentication API.")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "User Authentication",
            description = "Authenticate the user and return a JWT token if the user is valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The JWT token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid UserDTO user) {
        return new ResponseEntity<>(this.userService.generateToken(user), HttpStatus.valueOf(200));
    }

    @Operation(summary = "Create a new user in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format for the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PostMapping()
    public ResponseEntity<MessageDTO> create(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(this.userService.create(userDTO), HttpStatusCode.valueOf(201));
    }

}
