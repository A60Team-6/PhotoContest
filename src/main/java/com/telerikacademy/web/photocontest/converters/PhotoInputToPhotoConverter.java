package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoInput;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhotoInputToPhotoConverter implements Converter<PhotoInput, Photo> {
    @Override
    public Photo convert(PhotoInput source) {
        return Photo.builder()
                .title(source.getTitle())
                .story(source.getStory())
                .photoUrl(source.getPhotoUrl())
                .contest(source.getContest())
                .build();
    }
}
