package com.bexchauvet.vin.service;


import com.bexchauvet.lib.domain.User;
import com.bexchauvet.lib.repository.UserRepository;
import com.bexchauvet.vin.error.exception.BadLoginUnauthorizedException;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TokenDTO;
import com.bexchauvet.vin.rest.dto.UserDTO;
import com.bexchauvet.vin.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    JwtEncoder encoder;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(userRepository, encoder, passwordEncoder);
    }

    @Test
    @DisplayName("Test the generate token function")
    void generateToken() {
        when(userRepository.findByUsername("username2")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("Username")).thenReturn(Optional.of(new User(1L, "Username", "password",
                List.of("roles"), null, null)));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false).thenReturn(true);
        when(encoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(new Jwt("TOKEN", Instant.now(), Instant.now().plusSeconds(36000L),
                        Collections.singletonMap("Headers", "data"), Collections.singletonMap("claims", "data")));
        assertThrows(UsernameNotFoundException.class,
                () -> userService.generateToken(new UserDTO("username2", "password", List.of("roles"))));
        assertThrows(BadLoginUnauthorizedException.class,
                () -> userService.generateToken(new UserDTO("Username", "badpassword", List.of("roles"))));
        assertEquals(new TokenDTO("TOKEN"),
                userService.generateToken(new UserDTO("Username", "password", List.of("roles"))));
        verify(userRepository, times(3)).findByUsername(anyString());
        verify(passwordEncoder, times(2)).matches(anyString(), anyString());
        verify(encoder).encode(any(JwtEncoderParameters.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder, encoder);
    }

    @Test
    @DisplayName("Test the creation of user")
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(new User(1L, "username", "passwordEncoded",
                List.of("roles"), null, null));
        when(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded");
        MessageDTO expectedResult = new MessageDTO("User with ID 1 has been created", 1L);
        MessageDTO result = userService.create(new UserDTO("username", "password", List.of("roles")));
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(encoder);
    }

}
