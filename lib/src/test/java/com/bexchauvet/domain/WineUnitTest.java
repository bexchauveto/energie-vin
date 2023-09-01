package com.bexchauvet.domain;


import com.bexchauvet.lib.domain.Price;
import com.bexchauvet.lib.domain.TasteScore;
import com.bexchauvet.lib.domain.Wine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WineUnitTest {


    @Test
    @DisplayName("Test the validity of function expertCommentary and pastPrices")
    public void testWineCustomFuntion() {
        Instant now = Instant.now();
        Wine testWine = new Wine(null, "wine", "producer", 2023, "ROUGE", "FRANCE",
                new ArrayList<>(Arrays.asList(new TasteScore(null, null
                                , null, null, 0.0, "commentary1"),
                        new TasteScore(null, null, null, null, 0.0, "commentary2"))), 100.0,
                new ArrayList<>(Arrays.asList(new Price(null, null, null, now.minusSeconds(3600L), 10.0),
                        new Price(null, null, null, now, 12.2))), 12.2);
        List<String> expectedCommentary = List.of("commentary1", "commentary2");
        assertEquals(expectedCommentary, testWine.expertCommentary());
        Map<Instant, Double> expectedPastPrices = Map.of(now.minusSeconds(3600L), 10.0, now, 12.2);
        assertEquals(expectedPastPrices, testWine.pastPrices());
    }


}
