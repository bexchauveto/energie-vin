package com.bexchauvet.vin.service;

import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TasteScoreDTO;

import java.util.List;

public interface TasteScoreService {


    List<TasteScoreDTO> getAllTasteByUserID(String userID);

    TasteScoreDTO getID(String id, String userID);

    MessageDTO createTasteScore(TasteScoreDTO tasteScoreDTO, String userID);

    MessageDTO updateTasteScore(String id, TasteScoreDTO tasteScoreDTO, String userID);

    MessageDTO deleteTasteScore(String id, String userID);


}
