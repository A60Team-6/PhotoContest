package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.dtos.JuryPhotoRatingInputDto;
import com.telerikacademy.web.photocontest.models.dtos.JuryPhotoRatingOutputDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface JuryPhotoRatingService {
    List<JuryPhotoRatingOutputDto> getAllRatingsForPhoto(UUID photoId);
    JuryPhotoRatingOutputDto addRating(JuryPhotoRatingInputDto dto);
    void softDeleteRating(UUID id);

    List<JuryPhotoRatingOutputDto> getRatingsByUser(UUID userId);

    JuryPhotoRatingOutputDto updateRating(UUID id, JuryPhotoRatingInputDto dto);


    List<JuryPhotoRatingOutputDto> getRatingsForPhoto(UUID photoId);

    double getAverageScoreForPhoto(UUID photoId);
}
