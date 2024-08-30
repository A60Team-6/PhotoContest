package com.telerikacademy.web.photocontest.Managers;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.services.contracts.JuryPhotoRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContestScoreManager {

    private final JuryPhotoRatingService juryPhotoRatingService;


    public void setTotalScoreToEveryPhotoInTheContest(Contest contest) {
        Set<Photo> photos = contest.getPhotos();

        for (Photo photo : photos) {
            photo.setTotal_score(juryPhotoRatingService.getAverageScoreForPhoto(photo.getId()));
        }
    }

    public void decideTop3PlacesAndSetPointsToUsers(Contest contest) {
        if (contest.getPhase().getName().equals("Finished")) {
            setTotalScoreToEveryPhotoInTheContest(contest);

            Set<Photo> photos = contest.getPhotos();

            double maxScore = photos.stream()
                    .mapToDouble(Photo::getTotal_score)
                    .max()
                    .orElse(0);

            List<Photo> topPhotos = photos.stream()
                    .filter(photo -> photo.getTotal_score() == maxScore)
                    .toList();

            if (topPhotos.size() == 1) {
                Photo topPhoto = topPhotos.get(0);
                topPhoto.getUser().setPoints(topPhoto.getUser().getPoints() + 50);
            } else if (topPhotos.size() > 1) {
                topPhotos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + 40));
            }

            double secondMaxScore = photos.stream()
                    .filter(photo -> photo.getTotal_score() < maxScore)
                    .mapToDouble(Photo::getTotal_score)
                    .max()
                    .orElse(0);

            if (maxScore >= (secondMaxScore * 2)) {
                for (Photo photo : topPhotos) {
                    photo.getUser().setPoints(photo.getUser().getPoints() + 75);
                }
            }

            List<Photo> secondPlacePhotos = photos.stream()
                    .filter(photo -> photo.getTotal_score() == secondMaxScore)
                    .toList();

            if (secondPlacePhotos.size() == 1) {
                Photo secondPlacePhoto = secondPlacePhotos.get(0);
                secondPlacePhoto.getUser().setPoints(secondPlacePhoto.getUser().getPoints() + 35);
            } else if (secondPlacePhotos.size() > 1) {
                secondPlacePhotos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + 25));
            }


            double thirdMaxScore = photos.stream()
                    .filter(photo -> photo.getTotal_score() < secondMaxScore)
                    .mapToDouble(Photo::getTotal_score)
                    .max()
                    .orElse(0);

            List<Photo> thirdPlacePhotos = photos.stream()
                    .filter(photo -> photo.getTotal_score() == thirdMaxScore)
                    .toList();

            if (thirdPlacePhotos.size() == 1) {
                Photo thirdPlacePhoto = thirdPlacePhotos.get(0);
                thirdPlacePhoto.getUser().setPoints(thirdPlacePhoto.getUser().getPoints() + 20);
            } else if (thirdPlacePhotos.size() > 1) {
                thirdPlacePhotos.forEach(photo -> photo.getUser().setPoints(photo.getUser().getPoints() + 10));
            }
        }

    }

}
