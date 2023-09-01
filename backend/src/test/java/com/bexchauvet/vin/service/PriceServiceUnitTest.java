package com.bexchauvet.vin.service;


import com.bexchauvet.lib.repository.PriceRepository;
import com.bexchauvet.vin.service.Impl.PriceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceServiceUnitTest {

    PriceService priceService;
    @Mock
    PriceRepository priceRepository;
    private final Integer yearToKeep = 1;

    @BeforeEach
    void init() {
        priceService = new PriceServiceImpl(priceRepository, yearToKeep);
    }

    @Test
    @DisplayName("Test the remove older price function")
    public void testRemoveOlderPrice() {
        doNothing().when(priceRepository).deleteOlderThan(any(Instant.class));
        priceService.removeOlderPrice();
        verify(priceRepository).deleteOlderThan(any(Instant.class));
        verifyNoMoreInteractions(priceRepository);
    }
}
