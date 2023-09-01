package com.bexchauvet.ingester.mq;

import com.bexchauvet.lib.domain.Price;
import com.bexchauvet.lib.domain.TasteScore;
import com.bexchauvet.lib.domain.Wine;
import com.bexchauvet.lib.mq.WineMQ;
import com.bexchauvet.lib.repository.PriceRepository;
import com.bexchauvet.lib.repository.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WineReceiverUnitTest {


    WineReceiver wineReceiver;
    @Mock
    WineRepository wineRepository;
    @Mock
    PriceRepository priceRepository;

    @BeforeEach
    void init() {
        wineReceiver = new WineReceiver(wineRepository, priceRepository);
    }

    @Test
    @DisplayName("Test receiving already exists wine object from RabbitMQ")
    public void testReceiveAlreadyExistsWine() {
        Instant now = Instant.now();
        when(wineRepository.findByName(anyString())).thenReturn(Optional.of(new Wine(null, "wine", "producer", 2023,
                "ROUGE", "FRANCE", new ArrayList<>(List.of(new TasteScore(null, null
                        , null, null, 0.0, "commentary1"),
                new TasteScore(null, null, null, null, 0.0, "commentary2"))), 100.0,
                new ArrayList<>(List.of(new Price(null, null, null, now.minusSeconds(3600L), 12.2))), 12.2)));
        when(wineRepository.save(any(Wine.class))).thenReturn(new Wine());
        when(priceRepository.save(any(Price.class))).thenReturn(new Price());
        wineReceiver.receiver(new WineMQ("wine", "producer", 2023, "ROUGE", "FRANCE", "https://url", now, 10.0));
        verify(wineRepository).findByName(anyString());
        verify(wineRepository).save(any(Wine.class));
        verify(priceRepository).save(any(Price.class));
        verifyNoMoreInteractions(wineRepository, priceRepository);
    }

    @Test
    @DisplayName("Test receiving new wine object from RabbitMQ")
    public void testReceiveNewWine() {
        Instant now = Instant.now();
        when(wineRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(wineRepository.save(any(Wine.class))).thenReturn(new Wine());
        when(priceRepository.save(any(Price.class))).thenReturn(new Price());
        wineReceiver.receiver(new WineMQ("wine", "producer", 2023, "ROUGE", "FRANCE", "https://url", now, 10.0));
        verify(wineRepository).findByName(anyString());
        verify(wineRepository).save(any(Wine.class));
        verify(priceRepository).save(any(Price.class));
        verifyNoMoreInteractions(wineRepository, priceRepository);
    }
}
