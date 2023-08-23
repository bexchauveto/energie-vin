package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.vin.repository.WineRepository;
import com.bexchauvet.vin.rest.dto.WineDTO;
import com.bexchauvet.vin.service.WineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WineServiceImpl implements WineService {

    private final WineRepository wineRepository;

    @Override
    public List<WineDTO> getAllWines() {
        return this.wineRepository.findAll().stream()
                .map(wine -> new WineDTO(wine.getName(), wine.getProducer(), wine.getVintage(),
                        wine.getColor(), wine.getCountry(), wine.averageScore(), wine.latestPrice()))
                .collect(Collectors.toList());
    }
}
