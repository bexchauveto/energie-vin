package com.bexchauvet.winescrapper.mq;


import com.bexchauvet.lib.mq.WineMQ;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class WineSender {

    private final AmqpTemplate rabbitTemplate;
    private final Queue queue;

    public void send(WineMQ wineMQ) {
        rabbitTemplate.convertAndSend(queue.getName(), wineMQ);
        log.info("Sending wine bottle with name : " + wineMQ.getName());
    }

}
