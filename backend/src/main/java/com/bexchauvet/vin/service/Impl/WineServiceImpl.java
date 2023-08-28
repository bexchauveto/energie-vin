package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.vin.domain.Wine;
import com.bexchauvet.vin.error.exception.SearchWineDTOBadRequestException;
import com.bexchauvet.vin.error.exception.WineNotFoundException;
import com.bexchauvet.vin.repository.WineRepository;
import com.bexchauvet.vin.rest.dto.SearchWineDTO;
import com.bexchauvet.vin.rest.dto.WineDTO;
import com.bexchauvet.vin.service.WineService;
import com.bexchauvet.vin.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WineServiceImpl implements WineService {

    private final WineRepository wineRepository;

    @Override
    public List<WineDTO> getAllWines() {
        return this.wineRepository.findAll().stream()
                .map(wine -> new WineDTO(String.valueOf(wine.getWineId()), wine.getName(), wine.getProducer(), wine.getVintage(),
                        wine.getColor(), wine.getCountry(), wine.getAverageScore(), null,
                        wine.getCurrentPrice(), null))
                .collect(Collectors.toList());
    }

    @Override
    public List<WineDTO> getWinesByCriteria(SearchWineDTO searchWineDTO) {
        this.checkCriteria(searchWineDTO);
        return this.wineRepository.getByCriteria(searchWineDTO.getColors(), searchWineDTO.getCountries(),
                        searchWineDTO.getVintages(), searchWineDTO.getMinScore(), searchWineDTO.getMaxScore(),
                        searchWineDTO.getMinPrice(), searchWineDTO.getMaxPrice()).stream()
                .map(wine -> new WineDTO(String.valueOf(wine.getWineId()), wine.getName(), wine.getProducer(),
                        wine.getVintage(), wine.getColor(), wine.getCountry(), wine.getAverageScore(), null,
                        wine.getCurrentPrice(), null))
                .collect(Collectors.toList());
    }

    @Override
    public WineDTO getWineById(String id) {
        Wine wine = this.wineRepository.findById(id).orElseThrow(() -> new WineNotFoundException(id));
        return new WineDTO(String.valueOf(wine.getWineId()), wine.getName(), wine.getProducer(), wine.getVintage(),
                wine.getColor(), wine.getCountry(), wine.getAverageScore(), wine.expertCommentary(),
                wine.getCurrentPrice(), wine.pastPrices());
    }

    @Override
    public List<String> getCountries() {
        return this.wineRepository.getDistinctCountry();
    }

    @Override
    public Double currentMaxPrice() {
        return this.wineRepository.currentMaxPrice();
    }

    private void checkCriteria(SearchWineDTO searchWineDTO) throws SearchWineDTOBadRequestException {
        if (!searchWineDTO.getColors().stream().allMatch(Constants.VIN_COLORS::contains)) {
            throw new SearchWineDTOBadRequestException("Wine colors can only be ROUGE, BLANC or ROSE");
        }
        if (!searchWineDTO.getVintages().stream().allMatch(vintage -> (vintage < LocalDate.now().getYear() && vintage > 1900))) {
            throw new SearchWineDTOBadRequestException("Wine vintages can only be between 1900 and " + LocalDate.now().getYear());
        }
        if (searchWineDTO.getMinScore() < 0.0) {
            throw new SearchWineDTOBadRequestException("The minimum score cannot be lower than 0.0");
        }
        if (searchWineDTO.getMaxScore() > 100.0) {
            throw new SearchWineDTOBadRequestException("The maximum score cannot be higher than 100.0");
        }
        if (searchWineDTO.getMaxScore() <= searchWineDTO.getMinScore()) {
            throw new SearchWineDTOBadRequestException("The maximum score cannot be lower or equals than the minimum " +
                    "score");
        }
        if (searchWineDTO.getMinPrice() < 0.0) {
            throw new SearchWineDTOBadRequestException("The minimum price cannot be lower than 0.0");
        }
        if (searchWineDTO.getMaxPrice() <= searchWineDTO.getMinPrice()) {
            throw new SearchWineDTOBadRequestException("The maximum price cannot be lower or equals than the minimum " +
                    "price");
        }
    }

}
