package com.telerikacademy.web.photocontest.services;


import com.telerikacademy.web.photocontest.entities.*;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.JuryPhotoRatingRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.contracts.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JuryPhotoRatingRepository juryPhotoRatingRepository;
    private final PhotoService photoService;


    @Override
    public List<ContestOutput> getAllActive() {
        List<Contest> contests = contestRepository.findAllByIsActiveTrue();
        return contests.stream().map(contest -> conversionService.convert(contest, ContestOutput.class)).collect(Collectors.toList());
    }

    @Override
    public List<FinishedContestAntItsWinner> getAllUnActive() {
        List<Contest> contests = contestRepository.findAllByIsActiveFalse();
        List<FinishedContestAntItsWinner> finishedContestAntItsWinners = new ArrayList<>();
        for(Contest contest : contests){
            List<PhotoOutput> photosOnFirstPlace = findPhotosOnFirstPlace(contest);
            ContestOutput contestOutput = conversionService.convert(contest, ContestOutput.class);
            finishedContestAntItsWinners.add(FinishedContestAntItsWinner.builder()
                    .contestOutput(contestOutput)
                    .photoOutputList(photosOnFirstPlace)
                    .build());
        }


        return finishedContestAntItsWinners;
    }

    @Override
    public Page<Contest> getAllActiveContestInPhase1(String title, String category, int page, int size, String sortBy, String sortDirection){
       Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
       Pageable pageable = PageRequest.of(page, size, sort);

        String titleLike = title != null ? "%" + title + "%" : null;
        String categoryLike = category != null ? "%" + category + "%" : null;
        return contestRepository.findContestsPhase1ByMultipleFields(titleLike, categoryLike, pageable);
    }

//    @Override
//    public List<Contest> getAllActiveContestInPhase2(){
//        List<Contest> contests = contestRepository.findAllByIsActiveTrue();
//
//        return contests.stream().filter(contest -> contest.getPhase().getName().equals("Phase 2")).toList();
//    }

    @Override
    public Page<Contest> getAllActiveContestInPhase2(String title, String category, int page, int size, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        String titleLike = title != null ? "%" + title + "%" : null;
        String categoryLike = category != null ? "%" + category + "%" : null;
        return contestRepository.findContestsPhase2ByMultipleFields(titleLike, categoryLike, pageable);
    }

    @Override
    public List<ContestOutput> getAllContests() {
        List<Contest> contests = contestRepository.findAll();
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
                .changePhaseTime(LocalDateTime.now().plusHours(contestInput.getChangePhaseTime()))
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
                activeContests = contestRepository.findAllByIsActiveTrue();
            }
        }
    }

    public void changePhase(Contest contest) {
        String currentPhase = contest.getPhase().getName();
        LocalDateTime now = LocalDateTime.now();
        Phase phase2 = phaseService.getPhaseByName("Phase 2");


        if ("Phase 1".equals(currentPhase)) {
            contest.setPhase(phase2);
            Duration duration = Duration.between(contest.getCreatedAt(), contest.getChangePhaseTime());
            long hours = duration.toHours();
            contest.setChangePhaseTime(now.plusHours(hours));
            System.out.println("Contest moved to Phase 2");
        } else if ("Phase 2".equals(currentPhase)) {
            contest.setPhase(phaseService.getPhaseByName("Finished"));
            System.out.println("Contest finished");
        }

//        contest.setChangePhaseTime(now);
        contestRepository.save(contest);
        if ("Finished".equals(contest.getPhase().getName())) {

            List<Photo> photos = photoRepository.findAllByContestAndIsActiveTrue(contest);
            List<User> jurors = userService.getAllUsersWithJuryRights();
            for (Photo photo : photos) {
                List<JuryPhotoRating> ratingsForPhoto = juryPhotoRatingService.getAllRatingsEntityForPhoto(photo.getId());
                for (User jury : jurors) {
                    boolean isThere = false;
                    for (JuryPhotoRating rating : ratingsForPhoto) {
                        if (rating.getJury().equals(jury)) {
                            isThere = true;
                        }

                    }
                    if (!isThere) {
                        JuryPhotoRating defaultRating = JuryPhotoRating.builder()
                                .photo(photo)
                                .jury(jury)
                                .score(3)
                                .comment("This jury did not rate your photo.")
                                .categoryMatch(true)
                                .reviewDate(LocalDateTime.now())
                                .isActive(true)
                                .build();
                        juryPhotoRatingRepository.save(defaultRating);


                        double averageScore = juryPhotoRatingService.getAverageScoreForPhoto(photo.getId());
                        photo.setTotal_score(averageScore);
                        photoRepository.save(photo);
                    }
                }
            }


            decideTop3PlacesAndSetPointsToUsers(contest);
            photos.forEach(photo -> changeRanking(photo.getUser()));
            contest.setIsActive(false);
            contestRepository.save(contest);


        }
    }

    public void decideTop3PlacesAndSetPointsToUsers(Contest contest) {

        List<Photo> photos = photoRepository.findAllByContestAndIsActiveTrue(contest);

        double maxScore = findMaxScore(photos);
        List<Photo> topPhotos = filterPhotosByScore(photos, maxScore);

        double secondMaxScore = findOtherScore(photos, maxScore);

        if (maxScore >= (secondMaxScore * 2)) {
            for (Photo photo : topPhotos) {
                photo.getUser().setPoints(photo.getUser().getPoints() + 75);
                userRepository.save(photo.getUser());
            }
        } else {
            assignPointsToPhotos(topPhotos, 50, 40);
        }

        List<Photo> secondPlacePhotos = filterPhotosByScore(photos, secondMaxScore);
        assignPointsToPhotos(secondPlacePhotos, 35, 25);


        double thirdMaxScore = findOtherScore(photos, secondMaxScore);
        List<Photo> thirdPlacePhotos = filterPhotosByScore(photos, thirdMaxScore);
        assignPointsToPhotos(thirdPlacePhotos, 20, 10);
    }

    public double findMaxScore(List<Photo> photos) {
        return photos.stream()
                .mapToDouble(Photo::getTotal_score)
                .max()
                .orElse(0);
    }

    public double findOtherScore(List<Photo> photos, double maxScore) {
        return photos.stream()
                .filter(photo -> photo.getTotal_score() < maxScore)
                .mapToDouble(Photo::getTotal_score)
                .max()
                .orElse(0);
    }

    public List<Photo> filterPhotosByScore(List<Photo> photos, double score) {
        return photos.stream()
                .filter(photo -> photo.getTotal_score() == score)
                .toList();
    }

    public void assignPointsToPhotos(List<Photo> photos, int singlePoints, int sharedPoints) {
        if (photos.size() == 1) {
            addPointsToUser(photos.get(0).getUser(), singlePoints);
        } else if (photos.size() > 1) {
            photos.forEach(photo -> addPointsToUser(photo.getUser(), sharedPoints));
        }
    }

    public void addPointsToUser(User user, int points) {
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
    }

    public void changeRanking(User user) {
        if (user.getPoints() > 1001) {
            user.setRank(rankService.getRankByName("WiseAndBenevolentPhotoDictator"));
            userRepository.save(user);
        } else if (user.getPoints() >= 151 && user.getPoints() <= 1000) {
            user.setRank(rankService.getRankByName("Master"));
            userRepository.save(user);
            //Jury invitation
        } else if (user.getPoints() >= 51 && user.getPoints() <= 150) {
            user.setRank(rankService.getRankByName("Enthusiast"));
            userRepository.save(user);
        }
    }

    public List<PhotoOutput> findPhotosOnFirstPlace(Contest contest){
        List<Photo> list = photoService.getAllPhotosEntityOfContest(contest);
        double maxScore = findMaxScore(list);
        List<Photo> listOfPhotosOnFirstPlace = new ArrayList<>();

        for(Photo photo : list){
            if(photo.getTotal_score() == maxScore){
                listOfPhotosOnFirstPlace.add(photo);
            }
        }

        return listOfPhotosOnFirstPlace.stream().map(photo -> conversionService.convert(photo, PhotoOutput.class)).collect(Collectors.toList());
    }
}
