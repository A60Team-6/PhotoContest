package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.Managers.ContestScoreManager;
import com.telerikacademy.web.photocontest.Managers.RankManager;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContestServiceImpl implements ContestService {


    private final ConversionService conversionService;
    private final ContestRepository contestRepository;
    private final PhaseService phaseService;
//    private final RankManager rankManager;
//    private final ContestScoreManager contestScoreManager;
//    private final JuryPhotoRatingService juryPhotoRatingService;


    @Override
    public List<ContestOutput> getAll() {
        List<Contest> contests = contestRepository.findAllByIsActiveTrue();
        return contests.stream().map(contest -> conversionService.convert(contest, ContestOutput.class)).collect(Collectors.toList());
    }

    @Override
    public ContestOutput findContestById(UUID contestId) {
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(contestId);
        if (contest == null) {
            throw new EntityNotFoundException("Contest with ID " + contestId + " not found.");
        }
        return conversionService.convert(contest, ContestOutput.class);
    }

    @Override
    public Contest findContestEntityById(UUID contestId) {
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(contestId);
        if (contest == null) {
            throw new EntityNotFoundException("Contest with ID " + contestId + " not found.");
        }
        return contest;
    }

    @Override
    public ContestOutput findContestByTitle(String title) {
        Contest contest = contestRepository.findByTitleAndIsActiveTrue(title);
        return conversionService.convert(contest, ContestOutput.class);
    }

    @Override
    public ContestOutputId createContest(ContestInput contestInput, User user) {
        // Contest contest = conversionService.convert(contestInput, Contest.class);
        Contest contest = Contest.builder()
                .title(contestInput.getTitle())
                .category(contestInput.getCategory())
                .phase(phaseService.getPhaseByName("Phase 1"))
                .createdAt(LocalDateTime.now())
                .changePhaseTime(LocalDateTime.now())
                .isActive(true)
                .build();

        if (contest == null) {
            throw new EntityNotFoundException("Contest not found");
        }
        contest.setOrganizer(user);

        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByTitleAndIsActiveTrue(contest.getTitle());

        if (existingContest != null) {
            throw new DuplicateEntityException("Contest", "title", contest.getTitle());
        }

        contestRepository.save(contest);
        return conversionService.convert(contest, ContestOutputId.class);
    }

    @Override
    public void deactivateContest(UUID contestId, User user) {
        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByContestIdAndIsActiveTrue(contestId);

        if (existingContest == null) {
            throw new EntityNotFoundException("This contest does not exist");
        }

        existingContest.setIsActive(false);
        contestRepository.save(existingContest);
    }

    @Scheduled(fixedRate = 60000)
    public void updateContestPhases() {
        System.out.println("Scheduled task running");
        List<Contest> activeContests = contestRepository.findAllByIsActiveTrue();
        if (activeContests.isEmpty()) {
            System.out.println("No active contests found");
        }
        for (Contest contest : activeContests) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(contest.getChangePhaseTime())) {
                System.out.println("Changing phase for contest: " + contest.getTitle());
                changePhase(contest);
            }
        }
    }

    private void changePhase(Contest contest) {
        String currentPhase = contest.getPhase().getName();
        LocalDateTime now = LocalDateTime.now();
        Phase phase2 = phaseService.getPhaseByName("Phase 2");


        if ("Phase 1".equals(currentPhase)) {
            contest.setPhase(phase2);
            contest.setChangePhaseTime(now.plusMinutes(3));
            System.out.println("Contest moved to Phase 2");
        } else if ("Phase 2".equals(currentPhase)) {
            contest.setPhase(phaseService.getPhaseByName("Finished"));
            System.out.println("Contest finished");
        }

        contest.setChangePhaseTime(now);
        contestRepository.save(contest);

//        if("Finished".equals(contest.getPhase().getName())){
//            contestScoreManager.decideTop3PlacesAndSetPointsToUsers(contest);
//            contest.getPhotos().forEach(photo -> rankManager.changeRanking(photo.getUser()));
//        }
    }


//    private void decideTop3PlacesAndSetPointsToUsers(Contest contest) {
//        if (contest.getPhase().getName().equals("Finished")) {
//            setTotalScoreToEveryPhotoInTheContest(contest);
//
//            Set<Photo> photos = contest.getPhotos();
//
//            double maxScore = photos.stream()
//                    .mapToDouble(Photo::getTotal_score)
//                    .max()
//                    .orElse(0);
//
//            List<Photo> topPhotos = photos.stream()
//                    .filter(photo -> photo.getTotal_score() == maxScore)
//                    .toList();
//
//            if (topPhotos.size() == 1) {
//                Photo topPhoto = topPhotos.get(0);
//                topPhoto.getUser().setPoints(topPhoto.getUser().getPoints() + 50);
//            } else if (topPhotos.size() > 1) {
//                topPhotos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + 40));
//            }
//
//            double secondMaxScore = photos.stream()
//                    .filter(photo -> photo.getTotal_score() < maxScore)
//                    .mapToDouble(Photo::getTotal_score)
//                    .max()
//                    .orElse(0);
//
//            if (maxScore >= (secondMaxScore * 2)) {
//                for (Photo photo : topPhotos) {
//                    photo.getUser().setPoints(photo.getUser().getPoints() + 75);
//                }
//            }
//
//            List<Photo> secondPlacePhotos = photos.stream()
//                    .filter(photo -> photo.getTotal_score() == secondMaxScore)
//                    .toList();
//
//            if (secondPlacePhotos.size() == 1) {
//                Photo secondPlacePhoto = secondPlacePhotos.get(0);
//                secondPlacePhoto.getUser().setPoints(secondPlacePhoto.getUser().getPoints() + 35);
//            } else if (secondPlacePhotos.size() > 1) {
//                secondPlacePhotos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + 25));
//            }
//
//
//            double thirdMaxScore = photos.stream()
//                    .filter(photo -> photo.getTotal_score() < secondMaxScore)
//                    .mapToDouble(Photo::getTotal_score)
//                    .max()
//                    .orElse(0);
//
//            List<Photo> thirdPlacePhotos = photos.stream()
//                    .filter(photo -> photo.getTotal_score() == thirdMaxScore)
//                    .toList();
//
//            if (thirdPlacePhotos.size() == 1) {
//                Photo thirdPlacePhoto = thirdPlacePhotos.get(0);
//                thirdPlacePhoto.getUser().setPoints(thirdPlacePhoto.getUser().getPoints() + 20);
//            } else if (thirdPlacePhotos.size() > 1) {
//                thirdPlacePhotos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + 10));
//            }
//        }
//    }

//    private void changeRanking(User user){
//        if(user.getPoints() > 1001){
//            user.setRank(rankService.getRankByName("WiseAndBenevolentPhotoDictator"));
//        }else if(user.getPoints() >= 151 && user.getPoints() <= 1000){
//            user.setRank(rankService.getRankByName("Master"));
//            //Jury invitation
//        }else if(user.getPoints() >= 51 && user.getPoints() <= 150){
//            user.setRank(rankService.getRankByName("Enthusiast"));
//        }
//    }

//    private void setTotalScoreToEveryPhotoInTheContest(Contest contest){
//        Set<Photo> photos = contest.getPhotos();
//
//        for(Photo photo : photos){
//            photo.setTotal_score(juryPhotoRatingService.getAverageScoreForPhoto(photo.getId()));
//        }
//    }

}
