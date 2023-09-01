package com.bexchauvet.vin.service;

import com.bexchauvet.lib.domain.TasteScore;
import com.bexchauvet.lib.domain.User;
import com.bexchauvet.lib.domain.Wine;
import com.bexchauvet.lib.repository.TasteScoreRepository;
import com.bexchauvet.lib.repository.UserRepository;
import com.bexchauvet.lib.repository.WineRepository;
import com.bexchauvet.vin.error.exception.TasteScoreNotFoundException;
import com.bexchauvet.vin.error.exception.WineNotFoundException;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TasteScoreDTO;
import com.bexchauvet.vin.service.Impl.TasteScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TasteScoreServiceUnitTest {

    TasteScoreService tasteScoreService;
    @Mock
    TasteScoreRepository tasteScoreRepository;
    @Mock
    WineRepository wineRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        tasteScoreService = new TasteScoreServiceImpl(tasteScoreRepository, wineRepository, userRepository);
    }

    @Test
    @DisplayName("Test the get all taste score by the expert username")
    public void testGetAllTasteByUsername() {
        Instant now = Instant.now();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(tasteScoreRepository.findByUser(any(User.class))).thenReturn(
                List.of(new TasteScore(null, null, new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L), 98.0, "commentary1"),
                        new TasteScore(null, null, new Wine(1L, null, null, null, null, null, null, null, null, null), now,
                                65.0, "commentary2"))
        );
        List<TasteScoreDTO> expectedResult = List.of(
                new TasteScoreDTO("23", now.minusSeconds(3600L), 98.0, "commentary1"),
                new TasteScoreDTO("1", now, 65.0, "commentary2")
        );
        assertEquals(expectedResult, tasteScoreService.getAllTasteByUsername("username"));
        verify(userRepository).findByUsername(anyString());
        verify(tasteScoreRepository).findByUser(any(User.class));
        verifyNoInteractions(wineRepository);
        verifyNoMoreInteractions(userRepository, tasteScoreRepository);
    }

    @Test
    @DisplayName("Test the get a taste score by id with the expert username")
    public void testGetByID() {
        Instant now = Instant.now();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User(1L, "username", "password", null, null, null)));
        when(tasteScoreRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new TasteScore(1L, new User(12L, "username2", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L), 98.0, "commentary1")))
                .thenReturn(Optional.of(new TasteScore(11L, new User(1L, "username", "password", null, null, null), new Wine(23L,
                        null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L), 98.0, "commentary1")));
        TasteScoreDTO expectedResult = new TasteScoreDTO("23", now.minusSeconds(3600L), 98.0, "commentary1");
        assertThrows(TasteScoreNotFoundException.class, () -> tasteScoreService.getID("111", "username"));
        assertThrows(TasteScoreNotFoundException.class, () -> tasteScoreService.getID("1", "username"));
        assertEquals(expectedResult, tasteScoreService.getID("1", "username"));
        verify(userRepository, times(3)).findByUsername(anyString());
        verify(tasteScoreRepository, times(3)).findById(anyString());
        verifyNoInteractions(wineRepository);
        verifyNoMoreInteractions(userRepository, tasteScoreRepository);
    }

    @Test
    @DisplayName("Test the creation of a taste score")
    public void testCreateTestScore() {
        Instant now = Instant.now();
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(new User(1L, "username", "password", null, null, null)));
        when(wineRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new Wine(23L, null, null, null, null, null, null, null, null, null)));
        when(tasteScoreRepository.save(any(TasteScore.class)))
                .thenReturn(new TasteScore(1L, new User(1L, "username", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L),
                        98.0, "commentary1"));
        when(wineRepository.save(any(Wine.class))).thenReturn(new Wine());
        assertThrows(WineNotFoundException.class, () -> tasteScoreService.createTasteScore(new TasteScoreDTO("1",
                now.minusSeconds(3600L), 98.0, "commentary1"), "username"));
        MessageDTO result = tasteScoreService.createTasteScore(new TasteScoreDTO("23",
                now.minusSeconds(3600L), 98.0, "commentary1"), "username");
        MessageDTO expectedResult = new MessageDTO("Taste score with ID 1 has been created", new TasteScoreDTO("23",
                now.minusSeconds(3600L), 98.0, "commentary1"));
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository, times(2)).findByUsername(anyString());
        verify(wineRepository, times(2)).findById(anyString());
        verify(tasteScoreRepository).save(any(TasteScore.class));
        verify(wineRepository).save(any(Wine.class));
        verifyNoMoreInteractions(userRepository, tasteScoreRepository, wineRepository);
    }

    @Test
    @DisplayName("Test the update of a taste score")
    public void testUpdateTestScore() {
        Instant now = Instant.now();
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User(1L, "username", "password", null, null, null)));
        when(userRepository.findByUsername("username2"))
                .thenReturn(Optional.of(new User(123L, "username2", "password", null, null, null)));
        when(wineRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new Wine(23L, null, null, null, null, null, new ArrayList<>(List.of((new TasteScore(1L, new User(1L, "username", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L),
                        98.0, "commentary1")))), null, null, null)));
        when(tasteScoreRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new TasteScore(1L, new User(1L, "username", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L),
                        98.0, "commentary1")));
        when(tasteScoreRepository.save(any(TasteScore.class)))
                .thenReturn(new TasteScore(1L, new User(1L, "username", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L),
                        98.0, "commentary1"));
        when(wineRepository.save(any(Wine.class))).thenReturn(new Wine());
        //Wine not found
        assertThrows(WineNotFoundException.class, () -> tasteScoreService.updateTasteScore("1", new TasteScoreDTO("1",
                now.minusSeconds(3600L), 98.0, "commentary1"), "username"));
        //Taste score not found
        assertThrows(TasteScoreNotFoundException.class, () -> tasteScoreService.updateTasteScore("1", new TasteScoreDTO("1",
                now.minusSeconds(3600L), 98.0, "commentary1"), "username"));
        //Taste score found by from different user
        assertThrows(TasteScoreNotFoundException.class, () -> tasteScoreService.updateTasteScore("1", new TasteScoreDTO("1",
                now.minusSeconds(3600L), 98.0, "commentary1"), "username2"));
        MessageDTO result = tasteScoreService.updateTasteScore("1", new TasteScoreDTO("23",
                now.minusSeconds(3600L), 95.0, "commentary2"), "username");
        MessageDTO expectedResult = new MessageDTO("Taste score with ID 1 has been updated", new TasteScoreDTO("23",
                now.minusSeconds(3600L), 95.0, "commentary2"));
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository, times(4)).findByUsername(anyString());
        verify(wineRepository, times(4)).findById(anyString());
        verify(tasteScoreRepository, times(3)).findById(anyString());
        verify(tasteScoreRepository).save(any(TasteScore.class));
        verify(wineRepository).save(any(Wine.class));
        verifyNoMoreInteractions(userRepository, tasteScoreRepository, wineRepository);
    }

    @Test
    @DisplayName("Test the deletion of a taste score")
    public void testDeleteTestScore() {
        Instant now = Instant.now();
        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User(1L, "username", "password", null, null, null)));
        when(userRepository.findByUsername("username2"))
                .thenReturn(Optional.of(new User(123L, "username2", "password", null, null, null)));
        when(wineRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new Wine(23L, null, null, null, null, null, new ArrayList<>(List.of(new TasteScore(1L, new User(1L, "username", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L),
                        98.0, "commentary1"))), null, null, null)));
        when(tasteScoreRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new TasteScore(1L, new User(1L, "username", "password", null, null, null),
                        new Wine(23L, null, null, null, null, null, null, null, null, null), now.minusSeconds(3600L),
                        98.0, "commentary1")));
        doNothing().when(tasteScoreRepository).delete(any(TasteScore.class));
        when(wineRepository.save(any(Wine.class))).thenReturn(new Wine());
        //Taste score not found
        assertThrows(TasteScoreNotFoundException.class, () -> tasteScoreService.deleteTasteScore("1", "username"));
        //Taste score found by from different user
        assertThrows(TasteScoreNotFoundException.class, () -> tasteScoreService.deleteTasteScore("1", "username2"));
        //Wine not found
        assertThrows(WineNotFoundException.class, () -> tasteScoreService.deleteTasteScore("1", "username"));
        MessageDTO result = tasteScoreService.deleteTasteScore("1", "username");
        MessageDTO expectedResult = new MessageDTO("Taste score with ID 1 has been deleted", 1L);
        assertEquals(expectedResult.getMessage(), result.getMessage());
        assertEquals(expectedResult.getData(), result.getData());
        verify(userRepository, times(4)).findByUsername(anyString());
        verify(wineRepository, times(2)).findById(anyString());
        verify(tasteScoreRepository, times(4)).findById(anyString());
        verify(tasteScoreRepository).delete(any(TasteScore.class));
        verify(wineRepository).save(any(Wine.class));
        verifyNoMoreInteractions(userRepository, tasteScoreRepository, wineRepository);
    }
}
