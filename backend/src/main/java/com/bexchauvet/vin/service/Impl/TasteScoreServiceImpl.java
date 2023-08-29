package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.lib.domain.TasteScore;
import com.bexchauvet.lib.domain.User;
import com.bexchauvet.lib.domain.Wine;
import com.bexchauvet.vin.error.exception.TasteScoreNotFoundException;
import com.bexchauvet.vin.error.exception.WineNotFoundException;
import com.bexchauvet.lib.repository.TasteScoreRepository;
import com.bexchauvet.lib.repository.UserRepository;
import com.bexchauvet.lib.repository.WineRepository;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TasteScoreDTO;
import com.bexchauvet.vin.service.TasteScoreService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TasteScoreServiceImpl implements TasteScoreService {

    private TasteScoreRepository tasteScoreRepository;
    private WineRepository wineRepository;
    private UserRepository userRepository;

    @Override
    public List<TasteScoreDTO> getAllTasteByUserID(String userID) {
        List<TasteScore> tasteScores = this.tasteScoreRepository.findByUserID(userID);
        return tasteScores.stream().map(tasteScore ->
                        new TasteScoreDTO(String.valueOf(tasteScore.getWine().getWineId()), tasteScore.getDate(),
                                tasteScore.getScore(), tasteScore.getCommentary()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TasteScoreDTO getID(String id, String userID) {
        User user = this.userRepository.findByUsername(userID).get();
        TasteScore tasteScore = this.tasteScoreRepository.findById(id)
                .orElseThrow(() -> new TasteScoreNotFoundException(id));
        if (!tasteScore.getExpert().getUserId().equals(user.getUserId())) {
            throw new TasteScoreNotFoundException(id);
        }
        return new TasteScoreDTO(String.valueOf(tasteScore.getWine().getWineId()),
                tasteScore.getDate(), tasteScore.getScore(), tasteScore.getCommentary());
    }

    @Override
    @Transactional
    public MessageDTO createTasteScore(TasteScoreDTO tasteScoreDTO, String userID) {
        User user = this.userRepository.findByUsername(userID).get();
        Wine wine = this.wineRepository.findById(tasteScoreDTO.getWineID())
                .orElseThrow(() -> new WineNotFoundException(tasteScoreDTO.getWineID()));
        TasteScore tasteScore = this.tasteScoreRepository.save(new TasteScore(null, user, wine, tasteScoreDTO.getDate(),
                tasteScoreDTO.getScore(), tasteScoreDTO.getCommentary()));
        wine.getTasteScores().add(tasteScore);
        wine.setAverageScore(wine.getTasteScores().stream().mapToDouble(TasteScore::getScore).sum()/wine.getTasteScores().size());
        this.wineRepository.save(wine);
        return new MessageDTO(String.format("Taste score with ID %s has been created", tasteScore.getTasteScoreId()),
                tasteScore);
    }

    @Override
    @Transactional
    public MessageDTO updateTasteScore(String id, TasteScoreDTO tasteScoreDTO, String userID) {
        User user = this.userRepository.findByUsername(userID).get();
        Wine wine = this.wineRepository.findById(tasteScoreDTO.getWineID())
                .orElseThrow(() -> new WineNotFoundException(tasteScoreDTO.getWineID()));
        TasteScore tasteScore = this.tasteScoreRepository.findById(id)
                .orElseThrow(() -> new TasteScoreNotFoundException(id));
        if (!tasteScore.getExpert().getUserId().equals(user.getUserId())) {
            throw new TasteScoreNotFoundException(id);
        }
        tasteScore.setDate(tasteScoreDTO.getDate());
        tasteScore.setScore(tasteScoreDTO.getScore());
        tasteScore.setCommentary(tasteScoreDTO.getCommentary());
        tasteScore = this.tasteScoreRepository.save(tasteScore);
        wine.setAverageScore(wine.getTasteScores().stream().mapToDouble(TasteScore::getScore).sum()/wine.getTasteScores().size());
        this.wineRepository.save(wine);
        return new MessageDTO(String.format("Taste score with ID %s has been updated", tasteScore.getTasteScoreId()),
                tasteScore);
    }

    @Override
    @Transactional
    public MessageDTO deleteTasteScore(String id, String userID) {
        User user = this.userRepository.findByUsername(userID).get();
        TasteScore tasteScore = this.tasteScoreRepository.findById(id)
                .orElseThrow(() -> new TasteScoreNotFoundException(id));
        if (!tasteScore.getExpert().getUserId().equals(user.getUserId())) {
            throw new TasteScoreNotFoundException(id);
        }
        Wine wine = this.wineRepository.findById(String.valueOf(tasteScore.getWine().getWineId()))
                .orElseThrow(() -> new WineNotFoundException(String.valueOf(tasteScore.getWine().getWineId())));
        this.tasteScoreRepository.delete(tasteScore);
        wine.getTasteScores().remove(tasteScore);
        wine.setAverageScore(wine.getTasteScores().stream().mapToDouble(TasteScore::getScore).sum()/wine.getTasteScores().size());
        this.wineRepository.save(wine);
        return new MessageDTO(String.format("Taste score with ID %s has been deleted", tasteScore.getTasteScoreId()),
                tasteScore.getTasteScoreId());
    }


}
