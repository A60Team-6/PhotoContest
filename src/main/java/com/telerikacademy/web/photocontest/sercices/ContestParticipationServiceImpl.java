package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.ContestParticipation;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.ContestParticipationRepository;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestParticipationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ContestParticipationServiceImpl implements ContestParticipationService {


    private final ContestParticipationRepository repository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;

    @Override
    public void participateInContest(User user, UUID id){
        boolean contestExistsAndIsActive = true;
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(id);
        if(contest == null){
            contestExistsAndIsActive = false;
        }
        if(!contestExistsAndIsActive){
            throw new EntityNotFoundException("Contest not found");
        }

        ContestParticipation contestParticipation = new ContestParticipation();
        contestParticipation.setContest(contest);
        contestParticipation.setUser(user);
        contestParticipation.setCreatedAt(LocalDateTime.now());
        contestParticipation.setPhotoUploaded(false);
        contestParticipation.setIsActive(true);
        contestParticipation.setScore(0);

        repository.save(contestParticipation);

    }




}
