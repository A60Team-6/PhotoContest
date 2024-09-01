package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.helpers.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.*;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import com.telerikacademy.web.photocontest.services.contracts.ContestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Builder
public class ContestParticipationServiceImpl implements ContestParticipationService {


    private final ContestParticipationRepository repository;
    //private final ContestService contestService;
    private final ContestRepository contestRepository;

    @Override
    public List<ContestParticipation> getAll() {
        return repository.findAllByIsActiveTrue();
    }

    @Override
    public void participateInContest(User user, UUID id){
        boolean contestExistsAndIsActive = true;
    //    Contest contest = contestService.findContestEntityById(id);
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(id);

        if(contest == null){
            contestExistsAndIsActive = false;
        }

        if(!contestExistsAndIsActive){
            throw new EntityNotFoundException("Contest not found");
        }

        if(!contest.getPhase().getName().equals("Phase 1")){
            throw new UnauthorizedOperationException("Time to participate in contest has expired!");
        }

        ContestParticipation contestParticipation = ContestParticipation.builder()
                .contest(contest)
                .user(user)
                .createdAt(LocalDateTime.now())
                .photoUploaded(false)
                .isActive(true)
                .score(0)
                .build();

        repository.save(contestParticipation);
    }
}
