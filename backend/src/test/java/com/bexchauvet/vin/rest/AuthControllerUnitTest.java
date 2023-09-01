package com.bexchauvet.vin.rest;

import com.bexchauvet.vin.error.exception.BadLoginUnauthorizedException;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TokenDTO;
import com.bexchauvet.vin.rest.dto.UserDTO;
import com.bexchauvet.vin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    AuthController authController;

    @Mock
    UserService userService;

    @BeforeEach
    void init() {
        authController = new AuthController(userService);
    }


    @Test
    @DisplayName("Test to check authentication with bad credentials")
    void login_BadCredentials() {
        when(userService.generateToken(any(UserDTO.class)))
                .thenThrow(new UsernameNotFoundException("username"));
        assertThrows(UsernameNotFoundException.class,
                () -> authController.login(new UserDTO("badUsername", "password", List.of("roles"))));
        verify(userService).generateToken(any(UserDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Test to check authentication with bad credentials")
    void login_BadPasswords() {
        when(userService.generateToken(any(UserDTO.class)))
                .thenThrow(new BadLoginUnauthorizedException());
        assertThrows(BadLoginUnauthorizedException.class,
                () -> authController.login(new UserDTO("Username", "badPassword", List.of("roles"))));
        verify(userService).generateToken(any(UserDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Test to check authentication with good credentials")
    void login_GoodCredentials() {
        when(userService.generateToken(any(UserDTO.class)))
                .thenReturn(new TokenDTO("TOKEN"));
        assertEquals(new ResponseEntity<>(new TokenDTO("TOKEN"), HttpStatusCode.valueOf(200)),
                authController.login(new UserDTO("Username", "Password", List.of("roles"))));
        verify(userService).generateToken(any(UserDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Test to create a new user")
    void createNewUser() {
        when(userService.create(any(UserDTO.class)))
                .thenReturn(new MessageDTO("User with ID 1 has been created", 1L));
        assertEquals(new ResponseEntity<>(new MessageDTO("User with ID 1 has been created", 1L),
                        HttpStatusCode.valueOf(201)),
                authController.create(new UserDTO("Username", "Password", List.of("roles"))));
        verify(userService).create(any(UserDTO.class));
        verifyNoMoreInteractions(userService);
    }

}
