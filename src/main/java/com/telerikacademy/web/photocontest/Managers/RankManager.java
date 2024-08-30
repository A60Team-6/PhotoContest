package com.telerikacademy.web.photocontest.Managers;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.services.contracts.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankManager {

    private final RankService rankService;

    public void changeRanking(User user){
        if(user.getPoints() > 1001){
            user.setRank(rankService.getRankByName("WiseAndBenevolentPhotoDictator"));
        }else if(user.getPoints() >= 151 && user.getPoints() <= 1000){
            user.setRank(rankService.getRankByName("Master"));
            //Jury invitation
        }else if(user.getPoints() >= 51 && user.getPoints() <= 150){
            user.setRank(rankService.getRankByName("Enthusiast"));
        }
    }
}
