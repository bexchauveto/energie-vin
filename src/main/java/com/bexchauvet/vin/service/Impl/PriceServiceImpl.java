package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.vin.repository.PriceRepository;
import com.bexchauvet.vin.service.PriceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final Integer yearToKeep;

    public PriceServiceImpl(PriceRepository priceRepository, @Value("${app.year-to-keep}") Integer yearToKeep) {
        this.priceRepository = priceRepository;
        this.yearToKeep = yearToKeep;
    }


    @Override
    @Transactional
    @Scheduled(cron = "${app.cron.remove-older-price}")
    public void removeOlderPrice() {
        //Delete price older than one year
        this.priceRepository.deleteOlderThan(Instant.now().minusSeconds(yearToKeep * 31536000L));
    }
}
