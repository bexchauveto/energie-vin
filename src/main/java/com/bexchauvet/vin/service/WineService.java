package com.bexchauvet.vin.service;

import com.bexchauvet.vin.rest.dto.SearchWineDTO;
import com.bexchauvet.vin.rest.dto.WineDTO;

import java.util.List;

public interface WineService {

    List<WineDTO> getAllWines();

    List<WineDTO> getWinesByCriteria(SearchWineDTO searchWineDTO);

    WineDTO getWineById(String id);

    List<String> getCountry();
}
