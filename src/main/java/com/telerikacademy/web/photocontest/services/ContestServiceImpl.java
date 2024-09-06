package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestService;
import com.telerikacademy.web.photocontest.services.contracts.JuryPhotoRatingService;
import com.telerikacademy.web.photocontest.services.contracts.PhaseService;
import com.telerikacademy.web.photocontest.services.contracts.RankService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContestServiceImpl implements ContestService {


    private final ConversionService conversionService;
    private final ContestRepository contestRepository;
    private final PhaseService phaseService;
    private final RankService rankService;
    private final JuryPhotoRatingService juryPhotoRatingService;


    @Override
    public List<ContestOutput> getAll() {
        List<Contest> contests = contestRepository.findAllByIsActiveTrue();
        return contests.stream()
                .map(contest -> conversionService.convert(contest, ContestOutput.class))
                .collect(Collectors.toList());
    }

    // ToDo return optionals from repository
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

// ToDo can you explain me this?
        if (contest == null) {
            throw new EntityNotFoundException("Contest not found");
        }
        // ToDo why this is not in the builder?
        contest.setOrganizer(user);
// ToDo you first set organizer and then check ?
        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByTitleAndIsActiveTrue(contest.getTitle());

        //ToDo use optionals
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
// ToDo optionals...
        if (existingContest == null) {
            throw new EntityNotFoundException("This contest does not exist");
        }

        existingContest.setIsActive(false);
        contestRepository.save(existingContest);
    }
    // ToDo what happened here?

//    @Scheduled(fixedRate = 60000)
//    public void updateContestPhases() {
//        System.out.println("Scheduled task running");
//        List<Contest> activeContests = contestRepository.findAllByIsActiveTrue();
//        if (activeContests.isEmpty()) {
//            System.out.println("No active contests found");
//        }
//        for (Contest contest : activeContests) {
//            LocalDateTime now = LocalDateTime.now();
//            if (now.isAfter(contest.getChangePhaseTime())) {
//                System.out.println("Changing phase for contest: " + contest.getTitle());
//                changePhase(contest);
//            }
//        }
//    }

    // ToDo no usages, why?
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

        if ("Finished".equals(contest.getPhase().getName())) {
            decideTop3PlacesAndSetPointsToUsers(contest);
            contest.getPhotos().forEach(photo -> changeRanking(photo.getUser()));
        }
    }

    public void changeRanking(User user) {
        if (user.getPoints() > 1001) {
            user.setRank(rankService.getRankByName("WiseAndBenevolentPhotoDictator"));
        } else if (user.getPoints() >= 151 && user.getPoints() <= 1000) {
            user.setRank(rankService.getRankByName("Master"));
            //Jury invitation
        } else if (user.getPoints() >= 51 && user.getPoints() <= 150) {
            user.setRank(rankService.getRankByName("Enthusiast"));
        }
    }

    // ToDo check this refactoring
    public void setTotalScoreToEveryPhotoInTheContest(Contest contest) {
        contest.getPhotos()
                .parallelStream()
                .forEach(
                        p -> p.setTotal_score(juryPhotoRatingService.getAverageScoreForPhoto(p.getId()))
                );


    }

    public void decideTop3PlacesAndSetPointsToUsers(Contest contest) {
        if (!"Finished".equals(contest.getPhase().getName())) {
            return;
        }

        setTotalScoreToEveryPhotoInTheContest(contest);

        Set<Photo> photos = contest.getPhotos();

        Map<Double, List<Photo>> photosByScore = photos.stream()
                .collect(Collectors.groupingBy(Photo::getTotal_score));

        List<Double> sortedScores = photosByScore.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        List<Photo> firstPlacePhotos = getPhotosForPlace(sortedScores, photosByScore, 0);
        List<Photo> secondPlacePhotos = getPhotosForPlace(sortedScores, photosByScore, 1);
        List<Photo> thirdPlacePhotos = getPhotosForPlace(sortedScores, photosByScore, 2);

        assignPoints(firstPlacePhotos, 50, 40);
        assignPoints(secondPlacePhotos, 35, 25);
        assignPoints(thirdPlacePhotos, 20, 10);

        if (sortedScores.size() > 1 && sortedScores.get(0) >= sortedScores.get(1) * 2) {
            assignPoints(firstPlacePhotos, 75, 75);
        }
    }

    private List<Photo> getPhotosForPlace(List<Double> sortedScores, Map<Double, List<Photo>> photosByScore, int place) {
        return place < sortedScores.size()
                ? photosByScore.get(sortedScores.get(place))
                : Collections.emptyList();
    }

    private void assignPoints(List<Photo> photos, int singlePhotoPoints, int multiplePhotosPoints) {
        int points = photos.size() == 1
                ? singlePhotoPoints
                : multiplePhotosPoints;
        photos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + points));
    }


}
