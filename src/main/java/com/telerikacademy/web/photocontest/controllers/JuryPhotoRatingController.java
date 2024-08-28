package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.JuryPhotoRatingServiceImpl;
import com.telerikacademy.web.photocontest.services.contracts.JuryPhotoRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class JuryPhotoRatingController {

    private final JuryPhotoRatingService juryPhotoRatingService;
    private final AuthenticationHelper authenticationHelper;


    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<JuryPhotoRatingOutput>> getAllRatingsForPhoto(@PathVariable UUID photoId, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        List<JuryPhotoRatingOutput> ratings = juryPhotoRatingService.getAllRatingsForPhoto(photoId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuryPhotoRatingOutput> getRatingById(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(juryPhotoRatingService.getRatingById(id));
    }

    @PostMapping
    public ResponseEntity<JuryPhotoRatingOutput> createRating(@RequestBody JuryPhotoRatingInput input, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(juryPhotoRatingService.createRating(input));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteRating(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        juryPhotoRatingService.softDeleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuryPhotoRatingOutput> updateRating(@PathVariable UUID id, @RequestBody JuryPhotoRatingInput input, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(juryPhotoRatingService.updateRating(id, input));
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<JuryPhotoRatingOutput>> getRatingsByUser(@PathVariable UUID userId, @RequestHeader HttpHeaders headers) {
//        authenticationHelper.tryGetUser(headers);
//        List<JuryPhotoRatingOutput> ratings = juryPhotoRatingService.getRatingsByUser(userId);
//        return ResponseEntity.ok(ratings);
//    }


//    @GetMapping("/photo/{photoId}/user/{userId}")
//    public ResponseEntity<List<JuryPhotoRatingOutput>> getRatingsForPhoto(@PathVariable UUID photoId, @PathVariable UUID userId, @RequestHeader HttpHeaders headers) {
//        authenticationHelper.tryGetUser(headers);
//        List<JuryPhotoRatingOutput> rating = juryPhotoRatingService.getRatingsForPhoto(photoId, userId);
//        return ResponseEntity.ok(rating);
//    }

    @GetMapping("/photo/{photoId}/average-score")
    public ResponseEntity<Double> getAverageScoreForPhoto(@PathVariable UUID photoId, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        double averageScore = juryPhotoRatingService.getAverageScoreForPhoto(photoId);
        return ResponseEntity.ok(averageScore);
    }
}
