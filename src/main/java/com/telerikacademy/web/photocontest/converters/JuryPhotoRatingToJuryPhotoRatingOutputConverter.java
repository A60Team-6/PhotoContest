package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import org.springframework.core.convert.converter.Converter;

import org.springframework.stereotype.Component;

@Component
public class JuryPhotoRatingToJuryPhotoRatingOutputConverter implements Converter<JuryPhotoRating, JuryPhotoRatingOutput> {
    @Override
    public JuryPhotoRatingOutput convert(JuryPhotoRating source) {
        return JuryPhotoRatingOutput.builder()
                .photoId(source.getPhoto().getId())
                .userId(source.getJury().getUserId())
                .score(source.getScore())
                .comment(source.getComment())
                .categoryMatch(source.getCategoryMatch())
                .reviewDate(source.getReviewDate())
                .build();
    }
}
