//package com.telerikacademy.web.photocontest.converters;
//
//import com.telerikacademy.web.photocontest.entities.Contest;
//import com.telerikacademy.web.photocontest.entities.Photo;
//import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
//import com.telerikacademy.web.photocontest.entities.dtos.PhotoInput;
//import com.telerikacademy.web.photocontest.repositories.ContestRepository;
//import com.telerikacademy.web.photocontest.services.contracts.ContestService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class PhotoInputToPhotoConverter implements Converter<PhotoInput, Photo> {
//
//    private final ContestService contestService;
//    private final ContestRepository contestRepository;
//
//    @Override
//    public Photo convert(PhotoInput source) {
//        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(UUID.fromString(source.getContestId()));
////        ContestOutputDto contest = contestService.findContestById(UUID.fromString(source.getContestId()));
//        return Photo.builder()
//                .title(source.getTitle())
//                .story(source.getStory())
//                .contest(contest)
//                .build();
//    }
//}
