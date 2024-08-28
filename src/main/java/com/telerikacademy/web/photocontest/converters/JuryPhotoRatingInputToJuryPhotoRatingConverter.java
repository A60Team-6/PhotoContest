package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JuryPhotoRatingInputToJuryPhotoRatingConverter implements Converter<JuryPhotoRatingInput, JuryPhotoRating> {

    @Override
    public JuryPhotoRating convert(JuryPhotoRatingInput source) {
        return JuryPhotoRating.builder()
                .score(source.getScore())
                .comment(source.getComment())
                .categoryMatch(source.getCategoryMatch())
                .build();
    }
}
