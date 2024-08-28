package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.helpers.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.*;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ContestParticipationServiceImpl implements ContestParticipationService {


    private final ContestParticipationRepository repository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

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

        if(!contest.getPhase().getName().equals("Phase 1")){
            throw new UnauthorizedOperationException("Time to participate in contest has expired!");
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


    @Override
    public void uploadPhotoToTheContestCompetition(UUID contestParticipationId, String photoUrl){
        ContestParticipation contestParticipation = repository.getReferenceById(contestParticipationId);
        User user = contestParticipation.getUser();
        Contest contest = contestParticipation.getContest();

        if (!contest.getPhase().getName().equals("Phase 1 ") && !contestParticipation.isPhotoUploaded()){
            throw new UnauthorizedOperationException("You can not upload a photo to the contest!");
        }

        Set<Photo> photosOfUser = user.getPhotos();
        boolean photoExist = false;
        String photoForCompetition;
        for (Photo photo : photosOfUser){
            if(photo.getPhotoUrl().equals(photoUrl)){
                photoExist = true;
                photoForCompetition = photo.getPhotoUrl();
            }
        }

        if (!photoExist){
            throw new EntityNotFoundException("User have not such a photo");
        }


        Photo photo = photoRepository.findByPhotoUrl(photoUrl);
        if(photo.getContest() != null){
            throw new UnauthorizedOperationException("This photo is already participating in contest!");
        }
        photo.setContest(contest);
        contestParticipation.setPhotoUploaded(true);

    }







}
