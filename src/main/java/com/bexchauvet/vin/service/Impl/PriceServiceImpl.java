package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.vin.repository.PriceRepository;
import com.bexchauvet.vin.service.PriceService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@AllArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    @Value("${app.year-to-keep}")
    private final Integer yearToKeep;

    @Override
    @Transactional
    @Scheduled(cron = "${app.cron.remove-older-price}")
    public void removeOlderPrice() {
        //Delete price older than one year
        this.priceRepository.deleteOlderThan(Instant.now().minusSeconds(yearToKeep * 31536000L));
    }
}
