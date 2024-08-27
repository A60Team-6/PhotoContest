package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoOutput;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhotoToPhotoOutputConverter implements Converter<Photo, PhotoOutput> {
    @Override
    public PhotoOutput convert(Photo source) {
        return PhotoOutput.builder()
                .title(source.getTitle())
                .story(source.getStory())
                .photoUrl(source.getPhotoUrl())
                .contest(String.valueOf(source.getContest()))
                .user(source.getUser().getUsername())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
