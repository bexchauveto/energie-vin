package com.bexchauvet.vin.service;

import com.bexchauvet.vin.rest.dto.WineDTO;

import java.util.List;

public interface WineService {

    List<WineDTO> getAllWines();
}
