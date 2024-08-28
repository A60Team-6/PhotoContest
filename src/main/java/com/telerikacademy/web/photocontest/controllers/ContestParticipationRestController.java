package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contestsParticipation")
public class ContestParticipationRestController {

    private final ContestParticipationService contestParticipationService;
    private final AuthenticationHelper authenticationHelper;

    @PostMapping("/participate/{contestId}")
    public ResponseEntity<String> participateInContest(@PathVariable UUID contestId, @RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            contestParticipationService.participateInContest(user, contestId);
            return new ResponseEntity<>("This user started participate in contest!", HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (UnsupportedOperationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

//    @PostMapping("/uploadPhoto/{contestId}")
//    public ResponseEntity<String> uploadPhotoToTheContestCompetition(@PathVariable UUID contestId, @RequestParam String photoUrl, @RequestHeader HttpHeaders headers){
//        try {
//            authenticationHelper.tryGetUser(headers);
//            contestParticipationService.uploadPhotoToTheContestCompetition(contestId, photoUrl);
//            return new ResponseEntity<>("This user uploaded a photo to the contest!", HttpStatus.OK);
//        }catch (EntityNotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }catch (UnsupportedOperationException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }


}
