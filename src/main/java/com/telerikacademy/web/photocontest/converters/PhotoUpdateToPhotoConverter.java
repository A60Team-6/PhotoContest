package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhotoUpdateToPhotoConverter implements Converter<PhotoUpdate, Photo> {

    @Override
    public Photo convert(PhotoUpdate source) {
        return Photo.builder()
                .title(source.getTitle())
                .story(source.getStory())
                .build();
    }
}
