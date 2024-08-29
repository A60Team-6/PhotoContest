package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoIdOutput;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhotoToPhotoIdOutputConverter implements Converter<Photo, PhotoIdOutput> {
    @Override
    public PhotoIdOutput convert(Photo source) {
        return PhotoIdOutput.builder()
                .id(source.getId().toString())
                .build();
    }
}
