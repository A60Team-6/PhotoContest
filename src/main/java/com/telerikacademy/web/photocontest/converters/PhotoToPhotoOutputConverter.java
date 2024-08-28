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
                .id(source.getId().toString())
                .build();
    }
}
