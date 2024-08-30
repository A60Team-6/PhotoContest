package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.services.JuryPhotoRatingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class JuryPhotoRatingRestController {

    private final JuryPhotoRatingServiceImpl juryPhotoRatingService;

//    @Autowired
//    public JuryPhotoRatingRestController(JuryPhotoRatingServiceImpl juryPhotoRatingService) {
//        this.juryPhotoRatingService = juryPhotoRatingService;
//    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<JuryPhotoRatingOutput>> getRatingsForPhoto(@PathVariable UUID photoId) {
        List<JuryPhotoRatingOutput> ratings = juryPhotoRatingService.getAllRatingsForPhoto(photoId);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping
    public ResponseEntity<JuryPhotoRatingOutput> addRating(@RequestBody JuryPhotoRatingInput dto) {
        JuryPhotoRatingOutput rating = juryPhotoRatingService.createRating(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable UUID id) {
        juryPhotoRatingService.softDeleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuryPhotoRatingOutput> updateRating(@PathVariable UUID id, @RequestBody JuryPhotoRatingInput dto) {
        JuryPhotoRatingOutput updatedRating = juryPhotoRatingService.updateRating(id, dto);
        return ResponseEntity.ok(updatedRating);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JuryPhotoRatingOutput>> getRatingsByUser(@PathVariable UUID userId) {
        List<JuryPhotoRatingOutput> ratings = juryPhotoRatingService.getRatingsByUser(userId);
        return ResponseEntity.ok(ratings);
    }


    @GetMapping("/photo/{photoId}/average-score")
    public ResponseEntity<Double> getAverageScoreForPhoto(@PathVariable UUID photoId) {
        double averageScore = juryPhotoRatingService.getAverageScoreForPhoto(photoId);
        return ResponseEntity.ok(averageScore);
    }


}
