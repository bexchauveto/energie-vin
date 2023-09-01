package com.bexchauvet.vin.service;

import com.bexchauvet.lib.domain.Price;
import com.bexchauvet.lib.domain.TasteScore;
import com.bexchauvet.lib.domain.Wine;
import com.bexchauvet.lib.repository.WineRepository;
import com.bexchauvet.vin.error.exception.SearchWineDTOBadRequestException;
import com.bexchauvet.vin.error.exception.WineNotFoundException;
import com.bexchauvet.vin.rest.dto.SearchWineDTO;
import com.bexchauvet.vin.rest.dto.WineDTO;
import com.bexchauvet.vin.service.Impl.WineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WineServiceUnitTest {

    WineService wineService;

    @Mock
    WineRepository wineRepository;

    @BeforeEach
    void init() {
        wineService = new WineServiceImpl(wineRepository);
    }


    @Test
    @DisplayName("Test the get wines function")
    public void testGetWines() {
        when(wineRepository.findAll()).thenReturn(List.of(new Wine(1L, "name", "producer", 2023, "ROUGE", "FRANCE",
                null, 98.0, null, 12.3)));
        List<WineDTO> expectedResult = List.of(new WineDTO("1", "name", "producer", 2023, "ROUGE", "FRANCE",
                98.0, null, 12.3, null));
        assertEquals(expectedResult, wineService.getAllWines());
        verify(wineRepository).findAll();
        verifyNoMoreInteractions(wineRepository);
    }

    @Test
    @DisplayName("Test the get wines by criteria function")
    public void testGetWinesByCriteria() {
        // Bad Wine colors
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("RED"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 100.0, 0.0, 100.0)));
        // Bad years
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"),
                        List.of(1800), 0.0, 100.0, 0.0, 100.0)));
        // Bad years
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"),
                        List.of(21111), 0.0, 100.0, 0.0, 100.0)));
        // Bad min score
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , -100.0, 100.0, 0.0, 100.0)));
        // Bad max score
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 1110.0, 0.0, 100.0)));
        // Max score lower than min score
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 15.0, 10.0, 0.0, 100.0)));
        // Negative price
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 100.0, -120.0, 100.0)));
        // Max price lower than min price
        assertThrows(SearchWineDTOBadRequestException.class,
                () -> wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"), List.of(2023)
                        , 0.0, 100.0, 15.0, 10.0)));
        when(wineRepository.getByCriteria(anyList(), anyList(), anyList(), anyDouble(), anyDouble(), anyDouble(),
                anyDouble())).thenReturn(List.of(new Wine(1L, "name", "producer", 2023, "ROUGE", "FRANCE",
                null, 98.0, null, 12.3)));
        List<WineDTO> expectedResult = List.of(new WineDTO("1", "name", "producer", 2023, "ROUGE", "FRANCE",
                98.0, null, 12.3, null));
        assertEquals(expectedResult, wineService.getWinesByCriteria(new SearchWineDTO(List.of("ROUGE"), List.of("FRANCE"), List.of(2023), 0.0,
                100.0, 0.0, 100.0)));
        verify(wineRepository).getByCriteria(anyList(), anyList(), anyList(), anyDouble(), anyDouble(), anyDouble(),
                anyDouble());
        verifyNoMoreInteractions(wineRepository);
    }

    @Test
    @DisplayName("Test the get wine by ID function")
    public void testGetWineById() {
        Instant now = Instant.now();
        when(wineRepository.findById(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new Wine(1L, "name", "producer", 2023, "ROUGE", "FRANCE",
                        new ArrayList<>(List.of(new TasteScore(null, null, null, null, null, "commentary"))), 98.0,
                        new ArrayList<>(List.of(new Price(null, null, null, now.minusSeconds(3600), 12.3),
                                new Price(null, null, null, now, 10.3))),
                        10.3)));
        assertThrows(WineNotFoundException.class,
                () -> wineService.getWineById("2"));
        WineDTO expectedResult = new WineDTO("1", "name", "producer", 2023, "ROUGE", "FRANCE",
                98.0, List.of("commentary"), 10.3, Map.of(now.minusSeconds(3600), 12.3, now, 10.3));
        assertEquals(expectedResult, wineService.getWineById("1"));
        verify(wineRepository, times(2)).findById(anyString());
        verifyNoMoreInteractions(wineRepository);
    }


    @Test
    @DisplayName("Test the get countries function")
    public void testGetCountries() {
        when(wineRepository.getDistinctCountry()).thenReturn(List.of("FRANCE", "SPAIN", "ITALY"));
        List<String> expectedResult = List.of("FRANCE", "SPAIN", "ITALY");
        assertEquals(expectedResult, wineService.getCountries());
        verify(wineRepository).getDistinctCountry();
        verifyNoMoreInteractions(wineRepository);
    }

    @Test
    @DisplayName("Test the get current max price function")
    public void testGetCurrentMaxPrice() {
        when(wineRepository.currentMaxPrice()).thenReturn(1679.54);
        Double expectedResult = 1679.54;
        assertEquals(expectedResult, wineService.currentMaxPrice());
        verify(wineRepository).currentMaxPrice();
        verifyNoMoreInteractions(wineRepository);
    }

}
