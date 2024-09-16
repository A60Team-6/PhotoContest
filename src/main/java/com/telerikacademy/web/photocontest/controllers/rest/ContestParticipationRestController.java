package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participation")
public class ContestParticipationRestController {

    private final ContestParticipationService contestParticipationService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping
    public List<ContestParticipation> getAllContestParticipation() {
        return contestParticipationService.getAll();
    }


    @PostMapping("/{contestId}")
    public ResponseEntity<String> participateInContest(@PathVariable UUID contestId, @RequestHeader HttpHeaders httpHeaders) {
        User user = authenticationHelper.tryGetUser(httpHeaders);
        contestParticipationService.participateInContest(user, contestId);
        return new ResponseEntity<>("This user started participate in contest!", HttpStatus.OK);
    }
}
