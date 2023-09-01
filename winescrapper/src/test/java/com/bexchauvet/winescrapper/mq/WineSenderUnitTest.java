package com.bexchauvet.winescrapper.mq;

import com.bexchauvet.lib.mq.WineMQ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WineSenderUnitTest {

    WineSender wineSender;
    @Mock
    AmqpTemplate rabbitTemplate;
    @Mock
    Queue queue;

    @BeforeEach
    void init() {
        wineSender = new WineSender(rabbitTemplate, queue);
    }

    @Test
    @DisplayName("Test sending wineMQ object to RabbitMQ")
    public void testSendWine() {
        when(queue.getName()).thenReturn("wines");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(),any(WineMQ.class));
        wineSender.send(new WineMQ());
        verify(rabbitTemplate).convertAndSend(anyString(),any(WineMQ.class));
        verify(queue).getName();
        verifyNoMoreInteractions(queue, rabbitTemplate);
    }

}
