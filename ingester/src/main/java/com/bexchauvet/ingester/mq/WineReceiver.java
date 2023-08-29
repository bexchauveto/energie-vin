package com.bexchauvet.ingester.mq;

import com.bexchauvet.lib.domain.Price;
import com.bexchauvet.lib.domain.Wine;
import com.bexchauvet.lib.mq.WineMQ;
import com.bexchauvet.lib.repository.PriceRepository;
import com.bexchauvet.lib.repository.WineRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
@RabbitListener(queues = "wines", id = "listener")
public class WineReceiver{

    private final WineRepository wineRepository;
    private final PriceRepository priceRepository;
    @RabbitHandler
    public void receiver(WineMQ wineMQ) {
        Optional<Wine> wine = this.wineRepository.findByName(wineMQ.getName());
        if(wine.isPresent()) {
            if(wineMQ.getPrice() < wine.get().getCurrentPrice()){
                wine.get().setCurrentPrice(wineMQ.getPrice());
            }
            this.wineRepository.save(wine.get());
            this.priceRepository.save(new Price(null, wine.get(), wineMQ.getUrl(), Instant.now(), wineMQ.getPrice()));
            log.info(String.format("Updating Wine bottle under id %d with name %s", wine.get().getWineId(),
                    wine.get().getName()));
        } else {
            Wine newWine = this.wineRepository.save(new Wine(null, wineMQ.getName(), wineMQ.getProducer(), wineMQ.getVintage(),
                    wineMQ.getColor(), wineMQ.getCountry(), null, null, null, wineMQ.getPrice()));
            this.priceRepository.save(new Price(null, newWine, wineMQ.getUrl(), Instant.now(), wineMQ.getPrice()));
            log.info(String.format("Inserting new Wine bottle under id %d with name %s", newWine.getWineId(),
                    newWine.getName()));
        }
    }
}
