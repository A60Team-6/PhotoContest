package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;

import java.util.List;
import java.util.UUID;

public interface JuryPhotoRatingService {

    List<JuryPhotoRating> getAllRatingsEntityForPhoto(UUID photoId);

    JuryPhotoRatingOutput getRatingById(UUID id);

    JuryPhotoRatingOutput createRating(JuryPhotoRatingInput dto);

    void softDeleteRating(UUID id);

    JuryPhotoRatingOutput updateRating(UUID id, JuryPhotoRatingInput dto);


    List<JuryPhotoRatingOutput> getAllRatingsForPhoto(UUID photoId);

    List<JuryPhotoRatingOutput> getRatingsByUser(UUID userId);

//    List<JuryPhotoRatingOutput> getRatingsForPhoto(UUID photoId, UUID userId);

    double getAverageScoreForPhoto(UUID photoId);
}
