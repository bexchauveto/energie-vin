package com.bexchauvet.vin.rest;

import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TasteScoreDTO;
import com.bexchauvet.vin.service.TasteScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TasteScoreControllerUnitTest {

    TasteScoreController tasteScoreController;

    @Mock
    JwtDecoder jwtDecoder;
    @Mock
    TasteScoreService tasteScoreService;

    @BeforeEach
    void init() {
        tasteScoreController = new TasteScoreController(jwtDecoder, tasteScoreService);
    }


    @Test
    @DisplayName("Test the get all taste score by the expert username")
    public void testGetAllTasteByUsername() {
        Instant now = Instant.now();
        when(jwtDecoder.decode(anyString())).thenReturn(new Jwt("token", Instant.now(),
                Instant.now().plusSeconds(3600L), Map.of("header", "1"), Map.of("sub", "username")));
        when(tasteScoreService.getAllTasteByUsername(anyString()))
                .thenReturn(List.of(
                                new TasteScoreDTO("23", now.minusSeconds(3600L), 98.0, "commentary1"),
                                new TasteScoreDTO("1", now, 65.0, "commentary2")
                        )
                );
        List<TasteScoreDTO> expectedResult = List.of(
                new TasteScoreDTO("23", now.minusSeconds(3600L), 98.0, "commentary1"),
                new TasteScoreDTO("1", now, 65.0, "commentary2")
        );
        assertEquals(expectedResult, tasteScoreController.getAll(" username.token"));
        verify(tasteScoreService).getAllTasteByUsername(anyString());
        verify(jwtDecoder).decode(anyString());
        verifyNoMoreInteractions(tasteScoreService, jwtDecoder);
    }

    @Test
    @DisplayName("Test the get taste score by ID")
    public void testGetID() {
        Instant now = Instant.now();
        when(jwtDecoder.decode(anyString())).thenReturn(new Jwt("token", Instant.now(),
                Instant.now().plusSeconds(3600L), Map.of("header", "1"), Map.of("sub", "username")));
        when(tasteScoreService.getID(anyString(), anyString()))
                .thenReturn(
                        new TasteScoreDTO("23", now.minusSeconds(3600L), 98.0, "commentary1")

                );
        TasteScoreDTO expectedResult =
                new TasteScoreDTO("23", now.minusSeconds(3600L), 98.0, "commentary1");
        assertEquals(expectedResult, tasteScoreController.getId("23", " username.token"));
        verify(tasteScoreService).getID(anyString(), anyString());
        verify(jwtDecoder).decode(anyString());
        verifyNoMoreInteractions(tasteScoreService, jwtDecoder);
    }

    @Test
    @DisplayName("Test the create a taste score")
    public void testCreate() {
        Instant now = Instant.now();
        when(jwtDecoder.decode(anyString())).thenReturn(new Jwt("token", Instant.now(),
                Instant.now().plusSeconds(3600L), Map.of("header", "1"), Map.of("sub", "username")));
        when(tasteScoreService.createTasteScore(any(TasteScoreDTO.class), anyString()))
                .thenReturn(
                        new MessageDTO("Taste score with ID 1 has been created", new TasteScoreDTO("23",
                                now.minusSeconds(3600L), 98.0, "commentary1"))

                );
        MessageDTO expectedResult =
                new MessageDTO("Taste score with ID 1 has been created", new TasteScoreDTO("23",
                        now.minusSeconds(3600L), 98.0, "commentary1"));
        MessageDTO result = tasteScoreController.createScore(new TasteScoreDTO("23",
                now.minusSeconds(3600L), 98.0, "commentary1"), " username");
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(tasteScoreService).createTasteScore(any(TasteScoreDTO.class), anyString());
        verify(jwtDecoder).decode(anyString());
        verifyNoMoreInteractions(tasteScoreService, jwtDecoder);
    }

    @Test
    @DisplayName("Test the update a taste score")
    public void testUpdate() {
        Instant now = Instant.now();
        when(jwtDecoder.decode(anyString())).thenReturn(new Jwt("token", Instant.now(),
                Instant.now().plusSeconds(3600L), Map.of("header", "1"), Map.of("sub", "username")));
        when(tasteScoreService.updateTasteScore(anyString(), any(TasteScoreDTO.class), anyString()))
                .thenReturn(
                        new MessageDTO("Taste score with ID 1 has been updated", new TasteScoreDTO("23",
                                now.minusSeconds(3600L), 95.0, "commentary2"))

                );
        MessageDTO expectedResult =
                new MessageDTO("Taste score with ID 1 has been updated", new TasteScoreDTO("23",
                        now.minusSeconds(3600L), 95.0, "commentary2"));
        MessageDTO result = tasteScoreController.updateScore("1", new TasteScoreDTO("23",
                now.minusSeconds(3600L), 95.0, "commentary2"), " username");
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(tasteScoreService).updateTasteScore(anyString(), any(TasteScoreDTO.class), anyString());
        verify(jwtDecoder).decode(anyString());
        verifyNoMoreInteractions(tasteScoreService, jwtDecoder);
    }

    @Test
    @DisplayName("Test the delete a taste score")
    public void testDelete() {
        Instant now = Instant.now();
        when(jwtDecoder.decode(anyString())).thenReturn(new Jwt("token", Instant.now(),
                Instant.now().plusSeconds(3600L), Map.of("header", "1"), Map.of("sub", "username")));
        when(tasteScoreService.deleteTasteScore(anyString(), anyString()))
                .thenReturn(
                        new MessageDTO("Taste score with ID 1 has been deleted", 1L)

                );
        MessageDTO expectedResult =
                new MessageDTO("Taste score with ID 1 has been deleted", 1L);
        MessageDTO result = tasteScoreController.deleteScore("1", " username");
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(tasteScoreService).deleteTasteScore(anyString(), anyString());
        verify(jwtDecoder).decode(anyString());
        verifyNoMoreInteractions(tasteScoreService, jwtDecoder);
    }
}
