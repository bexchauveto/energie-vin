package com.bexchauvet.winescrapper.rest;


import com.bexchauvet.lib.mq.WineMQ;
import com.bexchauvet.winescrapper.mq.WineSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WineControllerUnitTest {

    WineController wineController;

    @Mock
    WineSender wineSender;

    @BeforeEach
    void init() {
        wineController = new WineController(wineSender);
    }


    @Test
    @DisplayName("Test sending wineMQ object to RabbitMQ wia rest API")
    public void testSendWine() {
        doNothing().when(wineSender).send(any(WineMQ.class));
        assertEquals("Message sent to the RabbitMQ Queue Successfully", wineController.createNewWine(new WineMQ()));
        verify(wineSender).send(any(WineMQ.class));
        verifyNoMoreInteractions(wineSender);
    }


}
